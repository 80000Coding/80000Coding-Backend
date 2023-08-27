package io.oopy.coding.global.security;

import io.oopy.coding.domain.user.repository.UserRepository;
import io.oopy.coding.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Cacheable(value = "user", key = "#user_id", unless = "#result == null")
    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.parseLong(user_id))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return CustomUserDetails.of(user);
    }
}
