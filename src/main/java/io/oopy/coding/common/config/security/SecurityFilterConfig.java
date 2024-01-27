package io.oopy.coding.common.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.oopy.coding.common.security.authentication.UserDetailServiceImpl;
import io.oopy.coding.common.security.filter.JwtAuthenticationFilter;
import io.oopy.coding.common.security.filter.JwtExceptionFilter;
import io.oopy.coding.common.security.jwt.JwtProvider;
import io.oopy.coding.common.security.jwt.qualifier.AccessTokenQualifier;
import io.oopy.coding.common.security.jwt.qualifier.RefreshTokenQualifier;
import io.oopy.coding.common.util.cookie.CookieUtil;
import io.oopy.coding.common.util.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.common.util.redis.refresh.RefreshTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class SecurityFilterConfig {
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    private final JwtProvider accessProvider;
    private final JwtProvider refreshProvider;
    private final CookieUtil cookieUtil;
    private final ObjectMapper objectMapper;

    SecurityFilterConfig (
        UserDetailServiceImpl userDetailServiceImpl,
        RefreshTokenService refreshTokenService,
        ForbiddenTokenService forbiddenTokenService,

        @AccessTokenQualifier JwtProvider accessProvider,
        @RefreshTokenQualifier JwtProvider refreshProvider,
        CookieUtil cookieUtil,
        ObjectMapper objectMapper
    ) {
        this.userDetailServiceImpl = userDetailServiceImpl;
        this.refreshTokenService = refreshTokenService;
        this.forbiddenTokenService = forbiddenTokenService;
        this.accessProvider = accessProvider;
        this.refreshProvider = refreshProvider;
        this.cookieUtil = cookieUtil;
        this.objectMapper = objectMapper;
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter(objectMapper);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthorizationFilter() {
        return new JwtAuthenticationFilter(userDetailServiceImpl, refreshTokenService, forbiddenTokenService, accessProvider, refreshProvider, cookieUtil);
    }

    @Bean
    ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
}
