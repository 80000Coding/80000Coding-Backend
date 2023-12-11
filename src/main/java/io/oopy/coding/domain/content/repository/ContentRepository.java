package io.oopy.coding.domain.content.repository;

import io.oopy.coding.domain.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findContentsByUserId(Long userId);
}
