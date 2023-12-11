package io.oopy.coding.domain.content.repository;

import io.oopy.coding.domain.content.entity.ContentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentCategoryRepository extends JpaRepository<ContentCategory, Long> {
    List<ContentCategory> findContentCategoriesByContentId(Long contentId);

    ContentCategory findContentCategoryByContentIdAndCategoryName(Long contentId, String categoryName);
}
