package io.oopy.coding.global.jwt;

import io.jsonwebtoken.JwtException;
import io.oopy.coding.global.security.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 지정한 URL 별로 JWT 유효성 검증을 수행하며, 직접적인 사용자 인증을 확인합니다.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;
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

        String jwtHeader = request.getHeader("Authorization");
        try {
            String token = jwtTokenProvider.resolveToken(jwtHeader);

            // TODO: test
            UserDetails userDetails = getUserDetails(jwtTokenProvider.getUserIdFromToken(token));
            authenticateUser(userDetails, request);
        } catch (JwtException e) {
            throw new ServletException(e);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isIgnoreUrlOrOptionRequest(List<?> jwtIgnoreUrls, String url, HttpServletRequest request) {
        return jwtIgnoreUrls.contains(url) || request.getMethod().equals("OPTIONS");
    }

    private UserDetails getUserDetails(Long userId) {
        if (Objects.isNull(userId)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

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
