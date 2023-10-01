package io.oopy.coding.common.jwt;

import io.oopy.coding.common.cookie.CookieUtil;
import io.oopy.coding.common.jwt.exception.auth.AuthErrorCode;
import io.oopy.coding.common.jwt.exception.auth.AuthErrorException;
import io.oopy.coding.common.redis.refresh.RefreshToken;
import io.oopy.coding.common.security.UserDetailServiceImpl;
import io.oopy.coding.common.jwt.entity.JwtUserInfo;
import io.oopy.coding.common.jwt.util.JwtTokenProvider;
import io.oopy.coding.common.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.common.redis.refresh.RefreshTokenService;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static io.oopy.coding.common.jwt.AuthConstants.*;

/**
 * 지정한 URL 별로 JWT 유효성 검증을 수행하며, 직접적인 사용자 인증을 확인합니다.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;

    private final List<String> jwtIgnoreUrls = List.of(
            "/test",
            "/api/v1/users/login",
            "/api/v1/users/refresh",
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger",
            "/favicon.ico",
            "/email/**",
            "/api/v1/login/oauth2/**",
            "/api/v1/signup"
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
        try {
            String token = jwtTokenProvider.resolveToken(authHeader);

            if (forbiddenTokenService.isForbidden(token))
                throw new AuthErrorException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN, "더 이상 사용할 수 없는 토큰입니다.");

            Date expiryDate =  jwtTokenProvider.getExpiryDate(token);
            return token;
        } catch (AuthErrorException e) {
            if (e.getErrorCode() == AuthErrorCode.EXPIRED_ACCESS_TOKEN) {
                log.warn("Expired JWT access token: {}", e.getMessage());
                return handleExpiredToken(request, response);
            }
            log.warn("Invalid JWT access token: {}", e.getMessage());

            ServletException se = new ServletException();
            se.initCause(e);
            throw se;
        }
    }

    private String handleExpiredToken(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ResponseCookie cookie = cookieUtil.deleteCookie(request, response, REFRESH_TOKEN.getValue())
                .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "Refresh token not found"));
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return reissueAccessToken(request, response);
    }

    private String reissueAccessToken(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Cookie refreshTokenCookie = cookieUtil.getCookie(request, REFRESH_TOKEN.getValue())
                    .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "Refresh token not found"));
            String requestRefreshToken = refreshTokenCookie.getValue();

            RefreshToken reissuedRefreshToken = refreshTokenService.refresh(requestRefreshToken);
            ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), reissuedRefreshToken.getToken(), refreshTokenCookie.getMaxAge());
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            JwtUserInfo userInfo = jwtTokenProvider.getUserInfoFromToken(requestRefreshToken);
            String reissuedAccessToken = jwtTokenProvider.generateAccessToken(userInfo);
            response.addHeader(ACCESS_TOKEN.getValue(), reissuedAccessToken);
            return reissuedAccessToken;
        } catch (AuthErrorException e) {
            ServletException se = new ServletException();
            se.initCause(e);
            throw se;
        }
    }

    private UserDetails getUserDetails(final String accessToken) {
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
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
}