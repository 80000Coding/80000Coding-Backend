package io.oopy.coding.domain.user.repository;

import io.oopy.coding.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByGithubId(Integer githubId);
    public boolean existsByGithubId(Integer githubId);

}