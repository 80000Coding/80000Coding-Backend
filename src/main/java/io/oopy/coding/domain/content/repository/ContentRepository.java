package io.oopy.coding.domain.content.repository;

import io.oopy.coding.domain.content.entity.Category;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.entity.ContentCategory;
import io.oopy.coding.domain.entity.User;
import io.oopy.coding.domain.mark.entity.ContentMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentRepository extends JpaRepository<Content, Long> {

    @Query("select c.user from Content c where c.id = :contentId")
    User findUserByUserId(@Param("contentId") Long contentId);

    @Query("select cat from Content c join c.contentCategories cc join cc.category cat where c.id = :contentId")
    Category findCategoryById(@Param("contentId") Long contentId);

    @Query("select cc from Content c join c.contentCategories cc where c.id = :contentId")
    ContentCategory findContentCategoryById(@Param("contentId") Long contentId);

    @Query("select cm from Content c join c.contentMarks cm where c.id = :contentId")
    ContentMark findContentMarkById(@Param("contentId") Long contentId);

}
