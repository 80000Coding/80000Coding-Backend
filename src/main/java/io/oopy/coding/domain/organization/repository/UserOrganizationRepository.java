package io.oopy.coding.domain.organization.repository;

import io.oopy.coding.domain.organization.entity.UserOrganization;
import io.oopy.coding.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Long> {
    List<UserOrganization> findByUser(User user);
}