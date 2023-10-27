package io.oopy.coding.domain.content.repository;

import io.oopy.coding.domain.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findContentsByUserId(Long userId);
}
