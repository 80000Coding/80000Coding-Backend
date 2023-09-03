package io.oopy.coding.domain.user.repository;

import io.oopy.coding.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public boolean existsByGithubId(int githubId);

}
