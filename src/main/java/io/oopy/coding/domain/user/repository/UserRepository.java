package io.oopy.coding.domain.user.repository;

import io.oopy.coding.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByGithubId(Integer githubId);
    boolean existsByGithubId(Integer githubId);
    boolean existsById(Long id);
    Optional<User> findByNameAndDeletedIsFalse(String name);
    Optional<User> findByIdAndDeletedIsFalse(Long id);
}
