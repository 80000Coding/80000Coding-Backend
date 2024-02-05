package io.oopy.coding.common.security.filter;

import io.oopy.coding.common.security.authentication.UserDetailServiceImpl;
import io.oopy.coding.common.security.jwt.JwtProvider;
import io.oopy.coding.common.security.jwt.dto.JwtSubInfo;
import io.oopy.coding.common.security.jwt.exception.AuthErrorCode;
import io.oopy.coding.common.security.jwt.exception.AuthErrorException;
import io.oopy.coding.common.util.cookie.CookieUtil;
import io.oopy.coding.common.util.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.common.util.redis.refresh.RefreshToken;
import io.oopy.coding.common.util.redis.refresh.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
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
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static io.oopy.coding.common.security.jwt.AuthConstants.*;

/**
 * 지정한 URL 별로 JWT 유효성 검증을 수행하며, 직접적인 사용자 인증을 확인합니다.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    private final JwtProvider accessProvider;
    private final JwtProvider refreshProvider;

    private final CookieUtil cookieUtil;

    private final List<String> jwtIgnoreUrls = List.of("/api/v1/auth/oauth/users/**");

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (shouldIgnoreRequest(request)) {
            log.info("Ignoring request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        if (isAnonymousRequest(request)) {
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
        return judge.isPresent() || "OPTIONS".equals(method);
    }

    private boolean isAnonymousRequest(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTH_HEADER.getValue());
        String refreshToken = request.getHeader(REFRESH_TOKEN.getValue());

        return accessToken == null && refreshToken == null;
    }

    private String resolveAccessToken(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String authHeader = request.getHeader(AUTH_HEADER.getValue());

        String token = accessProvider.resolveToken(authHeader);
        if (!StringUtils.hasText(token))
            handleAuthException(AuthErrorCode.EMPTY_ACCESS_TOKEN, "액세스 토큰이 없습니다.");

        if (forbiddenTokenService.isForbidden(token))
            handleAuthException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN, "더 이상 사용할 수 없는 토큰입니다.");

        if (accessProvider.isTokenExpired(token)) {
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

        JwtSubInfo userInfo = refreshProvider.getSubInfoFromToken(requestRefreshToken);
        String reissuedAccessToken = accessProvider.generateToken(userInfo);
        response.addHeader(REISSUED_ACCESS_TOKEN.getValue(), reissuedAccessToken);
        return reissuedAccessToken;
    }

    private UserDetails getUserDetails(final String accessToken) {
        Long userId = accessProvider.getSubInfoFromToken(accessToken).id();
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