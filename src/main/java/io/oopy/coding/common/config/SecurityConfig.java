package io.oopy.coding.common.config;

import io.oopy.coding.common.cookie.CookieUtil;
import io.oopy.coding.common.jwt.util.JwtTokenProvider;
import io.oopy.coding.common.jwt.handler.JwtAccessDeniedHandler;
import io.oopy.coding.common.jwt.handler.JwtAuthenticationEntryPoint;
import io.oopy.coding.common.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.common.redis.refresh.RefreshTokenService;
import io.oopy.coding.common.security.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final String[] webSecurityIgnoring = {
            "/",
            "/favicon.ico",
            "/api-docs/**",
            "/test/**",
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger",
            "/api/v1/users/login", "/api/v1/users/refresh",
            "/api/v1/login/oauth2/**", "/api/v1/signup"
    };

    private final UserDetailServiceImpl userDetailServiceImpl;
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(
                        auth -> {
                            try {
                                auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                        .requestMatchers(HttpMethod.OPTIONS, "*").permitAll()
                                        .requestMatchers(this.webSecurityIgnoring).permitAll()
                                        .anyRequest().authenticated();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint());
        httpSecurity.apply(new JwtSecurityConfig(userDetailServiceImpl, refreshTokenService, forbiddenTokenService, jwtTokenProvider, cookieUtil));
        return httpSecurity.build();
    }
}
