package io.oopy.coding.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.oopy.coding.global.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.global.redis.refresh.RefreshTokenService;
import io.oopy.coding.global.security.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
    private final ForbiddenTokenService forbiddenTokenService;

    private List<String> jwtIgnoreUrls = List.of(
            "/api/v1/users/login", "/api/v1/users/refresh"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isIgnoreUrlOrOptionRequest(jwtIgnoreUrls, request.getRequestURI(), request)) {
            log.info("isIgnoreUrlOrOptionRequest: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String jwtHeader = request.getHeader(AUTH_HEADER.getValue());
        String accessToken = resolveAccessToken(jwtHeader);

        // TODO: test
        UserDetails userDetails = getUserDetails(accessToken);
        authenticateUser(userDetails, request);

        filterChain.doFilter(request, response);
    }

    private boolean isIgnoreUrlOrOptionRequest(List<?> jwtIgnoreUrls, String url, HttpServletRequest request) {
        return jwtIgnoreUrls.contains(url) || request.getMethod().equals("OPTIONS");
    }

    private String resolveAccessToken(String jwtHeader) throws ServletException {
        try {
            return jwtTokenProvider.resolveToken(jwtHeader);
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException: {}", e.getMessage());
            String invalidToken = jwtTokenProvider.getTokenFromHeader(jwtHeader);

            // TODO: refresh Token이 만료되었을 경우 테스트 필요
            return refreshTokenService.refresh(invalidToken);
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
