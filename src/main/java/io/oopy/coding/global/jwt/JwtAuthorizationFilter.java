package io.oopy.coding.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.oopy.coding.global.jwt.entity.JwtUserInfo;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.global.cookie.CookieUtil;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorCode;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorException;
import io.oopy.coding.global.jwt.util.JwtTokenProvider;
import io.oopy.coding.global.redis.refresh.RefreshToken;
import io.oopy.coding.global.redis.refresh.RefreshTokenService;
import io.oopy.coding.global.security.CustomUserDetailService;
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

import static io.oopy.coding.global.jwt.AuthConstants.AUTH_HEADER;
import static io.oopy.coding.global.jwt.AuthConstants.REFRESH_TOKEN;

/**
 * 지정한 URL 별로 JWT 유효성 검증을 수행하며, 직접적인 사용자 인증을 확인합니다.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;

    private final List<String> jwtIgnoreUrls = List.of(
            "/api/v1/users/login"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (shouldIgnoreRequest(request)) {
            log.info("Ignoring request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveAccessToken(request, response);

        // TODO: test
        UserDetails userDetails = getUserDetails(accessToken);
        authenticateUser(userDetails, request);

        filterChain.doFilter(request, response);
    }

    private boolean shouldIgnoreRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        return jwtIgnoreUrls.contains(uri) || "OPTIONS".equals(method);
    }

    private String resolveAccessToken(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String authHeader = request.getHeader(AUTH_HEADER.getValue());
        try {
            String token = jwtTokenProvider.resolveToken(authHeader);
            Date expiryDate =  jwtTokenProvider.getExpiryDate(token);
            return token;
        } catch (ExpiredJwtException e) {
            return handleExpiredToken(request, response);
        } catch (JwtException e) {
            throw new ServletException(e);
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

            RefreshToken refreshToken = refreshTokenService.refresh(requestRefreshToken);
            ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), refreshToken.getToken(), refreshTokenCookie.getMaxAge());
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            JwtUserInfo userInfo = jwtTokenProvider.getUserInfoFromToken(requestRefreshToken);
            return jwtTokenProvider.generateAccessToken(userInfo);
        } catch (JwtException e) {
            throw new ServletException(e);
        }
    }

    private UserDetails getUserDetails(final String accessToken) {
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        return customUserDetailService.loadUserByUsername(userId.toString());
    }

    private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
