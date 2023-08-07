package io.oopy.coding.domain.user.dao;

import io.oopy.coding.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
