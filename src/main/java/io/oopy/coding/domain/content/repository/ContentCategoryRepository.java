package io.oopy.coding.domain.content.repository;

import io.oopy.coding.domain.content.entity.ContentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentCategoryRepository extends JpaRepository<ContentCategory, Long> {
}
