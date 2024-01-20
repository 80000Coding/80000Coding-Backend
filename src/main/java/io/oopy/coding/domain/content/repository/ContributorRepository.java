package io.oopy.coding.domain.content.repository;

import io.oopy.coding.domain.content.entity.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributorRepository extends JpaRepository<Contributor, Long> {
    boolean existsByUserIdAndContentId(Long userId, Long contentId);
    List<Contributor> findAllByContentId(Long contentId);
    Contributor deleteByContentIdAndUserId(Long conteId, Long userId);
}
