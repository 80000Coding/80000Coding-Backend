package io.oopy.coding.domain.content.dao;

import io.oopy.coding.domain.content.entity.ContentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentCategoryRepository extends JpaRepository<ContentCategory, Long> {
}
