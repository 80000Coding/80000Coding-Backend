package io.oopy.coding.global.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Exception caught in JwtExceptionFilter: {}", e.getMessage());
            e.getStackTrace();
            sendError(response, e);
        }
    }

    private void sendError(HttpServletResponse response, Exception e) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        AuthErrorResponse errorResponse = new AuthErrorResponse(UNAUTHORIZED.getReasonPhrase(), e.getMessage());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
