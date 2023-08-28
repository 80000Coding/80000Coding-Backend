package io.oopy.coding.global.config;

import io.oopy.coding.global.cookie.CookieUtil;
import io.oopy.coding.global.jwt.JwtAuthorizationFilter;
import io.oopy.coding.global.jwt.util.JwtTokenProvider;
import io.oopy.coding.global.jwt.exception.JwtExceptionFilter;
import io.oopy.coding.global.redis.refresh.RefreshTokenService;
import io.oopy.coding.global.security.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;



    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtAuthorizationFilter jwtAuthorizationFilter
                = new JwtAuthorizationFilter(jwtTokenProvider, userDetailServiceImpl, refreshTokenService, cookieUtil);
        JwtExceptionFilter jwtExceptionFilter = new JwtExceptionFilter();

        // TODO: test
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter, JwtAuthorizationFilter.class);
    }
}