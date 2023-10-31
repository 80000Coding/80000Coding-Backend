package io.oopy.coding.api.user.service;

import io.oopy.coding.domain.user.repository.UserRepository;
import io.oopy.coding.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSearchService {
    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 유저입니다.")
        );
    }

    public User findByGithubId(Integer githubId) {
        return userRepository.findByGithubId(githubId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 유저입니다.")
        );
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNameAndDeletedIsFalse(nickname).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 유저입니다.")
        );
    }

    public boolean isPresentByGithubId(Integer githubId) {
        return userRepository.existsByGithubId(githubId);
    }
}
