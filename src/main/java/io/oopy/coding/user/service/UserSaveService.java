package io.oopy.coding.user.service;

import io.oopy.coding.domain.user.repository.UserRepository;
import io.oopy.coding.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSaveService {
    private final UserRepository userRepository;
    @Transactional
    public User save(User user) {
        userRepository.save(user);

        return user;
    }
}
