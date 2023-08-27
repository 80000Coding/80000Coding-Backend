package io.oopy.coding.test;

import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {
    private final UserRepository userRepository;

    @Transactional
    public String getNowUserName() {
        User user = User.builder()
                .name("테스트" + randomNumber())
                .githubId(Integer.parseInt(randomNumber()))
                .bio("소개")
                .role(RoleType.USER)
                .build();
        User save = userRepository.save(user);
        return save.getName();
    }

    private String randomNumber() {
        return String.valueOf((int)(Math.random() * (99999 - 10000 + 1)) + 10000);
    }
}
