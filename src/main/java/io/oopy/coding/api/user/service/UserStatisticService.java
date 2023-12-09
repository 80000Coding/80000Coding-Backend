package io.oopy.coding.api.user.service;

import io.oopy.coding.domain.user.repository.UserQueryRepository;
import io.oopy.coding.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserStatisticService {
    private final UserQueryRepository userQueryRepository;

    public long countPostByUserId(long id) {
        long postCount = userQueryRepository.countPostByUserId(id);
        return postCount;
    }

    public long countProjectByUserId(long id) {
        long projCount = userQueryRepository.countProjByUserId(id);
        return projCount;
    }
}
