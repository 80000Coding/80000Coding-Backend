package io.oopy.coding.domain.email.repository;

import io.oopy.coding.domain.email.entity.EmailCertification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailCertificationRepository extends JpaRepository<EmailCertification, Long> {

    Optional<EmailCertification> findFirstByGithubIdOrderByExpiredAtDesc(String githubId);
}
