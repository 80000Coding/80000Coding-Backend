package io.oopy.coding.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * 지정한 URL 별로 JWT 유효성 검증을 수행하며, 직접적인 사용자 인증을 확인합니다.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private List<String> jwtIgnoreUrls = List.of(
            "/api/v1/users/login", "/api/v1/users/refresh"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isIgnoreUrlOrOptionRequest(jwtIgnoreUrls, request.getRequestURI(), request)) {
            log.info("isIgnoreUrlOrOptionRequest: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
        }


        try {
            String jwtHeader = request.getHeader("Authorization");
            String token = jwtTokenProvider.resolveToken(jwtHeader);

            // UserAuthentication 추가


            filterChain.doFilter(request, response);
        } catch (SignatureException | MalformedJwtException e) {
            log.error("SignatureException | MalformedJwtException: {}", e.getMessage());
            response.sendError(401, "Token is invalid");
        }
        catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Expired");
        } catch (Exception e) {
            log.error("Jwt Exception: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unexpected error");
        }
    }

    private boolean isIgnoreUrlOrOptionRequest(List<?> jwtIgnoreUrls, String url, HttpServletRequest request) {
        return jwtIgnoreUrls.contains(url) || request.getMethod().equals("OPTIONS");
    }
}
