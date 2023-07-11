package io.oopy.coding.domain.repository;

import io.oopy.coding.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
