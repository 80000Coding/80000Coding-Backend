package io.oopy.coding.common.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.oopy.coding.common.security.authentication.UserDetailServiceImpl;
import io.oopy.coding.common.security.filter.JwtAuthenticationFilter;
import io.oopy.coding.common.security.filter.JwtExceptionFilter;
import io.oopy.coding.common.util.cookie.CookieUtil;
import io.oopy.coding.common.security.jwt.JwtUtil;
import io.oopy.coding.common.util.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.common.util.redis.refresh.RefreshTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Configuration
public class SecurityFilterConfig {
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final ObjectMapper objectMapper;

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter(objectMapper);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthorizationFilter() {
        return new JwtAuthenticationFilter(userDetailServiceImpl, refreshTokenService, forbiddenTokenService, jwtUtil, cookieUtil);
    }

    @Bean
    ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
}
