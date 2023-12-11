package io.oopy.coding.common.security.filter;

import io.oopy.coding.common.util.cookie.CookieUtil;
import io.oopy.coding.common.util.jwt.exception.AuthErrorCode;
import io.oopy.coding.common.util.jwt.exception.AuthErrorException;
import io.oopy.coding.common.util.redis.refresh.RefreshToken;
import io.oopy.coding.common.security.authentication.UserDetailServiceImpl;
import io.oopy.coding.common.util.jwt.entity.JwtUserInfo;
import io.oopy.coding.common.util.jwt.JwtUtil;
import io.oopy.coding.common.util.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.common.util.redis.refresh.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static io.oopy.coding.common.util.jwt.AuthConstants.*;

/**
 * 지정한 URL 별로 JWT 유효성 검증을 수행하며, 직접적인 사용자 인증을 확인합니다.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    private final List<String> jwtIgnoreUrls = List.of(
            "/api/v1/users/test/**",
            "/api/v1/users/login",
            "/api/v1/users/refresh",
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger",
            "/api/v1/auth/login/**", "/api/v1/auth/signup",
            "/api/v1/profile/**",
            "/login/oauth2/**",
            "/api/v1/contents/get", "/api/v1/comments/get",
            "/favicon.ico",
            "/api/v1/feed/title", "/api/v1/feed/body",
            "/api/v1/category", "/api/v1/mark/get"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (shouldIgnoreRequest(request)) {
            log.info("Ignoring request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveAccessToken(request, response);

        UserDetails userDetails = getUserDetails(accessToken);
        authenticateUser(userDetails, request);
        filterChain.doFilter(request, response);
    }

    private boolean shouldIgnoreRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        Optional<String> judge = jwtIgnoreUrls.stream()
                .filter(v ->
                        Pattern.matches(v.replace("**", ".*"), uri) ||
                                Pattern.matches(v.replace("/**", ""), uri)
                )
                .findFirst();
        return !judge.isEmpty() || "OPTIONS".equals(method);
    }

    private String resolveAccessToken(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String authHeader = request.getHeader(AUTH_HEADER.getValue());

        String token = jwtUtil.resolveToken(authHeader);
        if (!StringUtils.hasText(token))
            handleAuthException(AuthErrorCode.EMPTY_ACCESS_TOKEN, "액세스 토큰이 없습니다.");

        if (forbiddenTokenService.isForbidden(token))
            handleAuthException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN, "더 이상 사용할 수 없는 토큰입니다.");

        if (jwtUtil.isTokenExpired(token)) {
            log.warn("Expired JWT access token: {}", token);
            return reissueAccessToken(request, response);
        }

        return token;
    }

    private String reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshTokenCookie = cookieUtil.getCookieFromRequest(request, REFRESH_TOKEN.getValue())
                .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "Refresh token not found"));
        String requestRefreshToken = refreshTokenCookie.getValue();

        RefreshToken reissuedRefreshToken = refreshTokenService.refresh(requestRefreshToken);
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), reissuedRefreshToken.getToken(), refreshTokenCookie.getMaxAge());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        JwtUserInfo userInfo = jwtUtil.getUserInfoFromToken(requestRefreshToken);
        String reissuedAccessToken = jwtUtil.generateAccessToken(userInfo);
        response.addHeader(REISSUED_ACCESS_TOKEN.getValue(), reissuedAccessToken);
        return reissuedAccessToken;
    }

    private UserDetails getUserDetails(final String accessToken) {
        Long userId = jwtUtil.getUserIdFromToken(accessToken);
        return userDetailServiceImpl.loadUserByUsername(userId.toString());
    }

    private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authenticated user: {}", userDetails.getUsername());
    }

    private void handleAuthException(AuthErrorCode errorCode, String errorMessage) throws ServletException {
        log.warn("AuthErrorException(code={}, message={})", errorCode.name(), errorMessage);
        AuthErrorException exception = new AuthErrorException(errorCode, errorMessage);
        throw new ServletException(exception);
    }
}