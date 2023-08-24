package io.oopy.coding.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.oopy.coding.global.cookie.CookieUtil;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorCode;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorException;
import io.oopy.coding.global.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.global.redis.refresh.RefreshTokenService;
import io.oopy.coding.global.security.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static io.oopy.coding.global.jwt.AuthConstants.ACCESS_TOKEN;
import static io.oopy.coding.global.jwt.AuthConstants.AUTH_HEADER;

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

        String accessToken = resolveAccessToken(request);

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

    private String resolveAccessToken(HttpServletRequest request) throws ServletException {
        Cookie cookie = cookieUtil.getCookie(request, ACCESS_TOKEN.getValue()).orElseThrow(
                () -> new AuthErrorException(AuthErrorCode.EMPTY_ACCESS_TOKEN, "Access Token is empty")
        );

        try {
            return jwtTokenProvider.resolveToken(cookie.getValue());
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException: {}", e.getMessage());

            // TODO: refresh Token이 만료되었을 경우 테스트 필요
            return refreshTokenService.refresh(cookie.getValue());
        } catch (JwtException e) {
            throw new ServletException(e);
        }
    }

    private UserDetails getUserDetails(String accessToken) {
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

        try {
            return customUserDetailService.loadUserByUsername(userId.toString());
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("can't find user");
        }
    }

    private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
