package io.oopy.coding.domain.content.repository;

import io.oopy.coding.domain.content.entity.ContentCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentCategoryRepository extends JpaRepository<ContentCategory, Long> {
    ContentCategory findContentCategoryByContentIdAndCategoryName(Long contentId, String categoryName);

    @EntityGraph(attributePaths = {"category"})
    @Query("select cc from ContentCategory cc where cc.content.id = :contentId")
    List<ContentCategory> findContentCategoriesWithCategoryByContentId(@Param("contentId") Long contentId);
}
