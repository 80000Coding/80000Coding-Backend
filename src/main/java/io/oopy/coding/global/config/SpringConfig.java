package io.oopy.coding.global.config;

import io.oopy.coding.domain.user.application.UserAuthService;
import io.oopy.coding.domain.user.application.UserSearchService;
import io.oopy.coding.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {
    private final UserRepository userRepository;

    @Bean
    public UserSearchService userSearch() {
        return new UserSearchService(userRepository);
    }
    @Bean
    public UserAuthService userAuth() {
        return new UserAuthService(userRepository);
    }
}
