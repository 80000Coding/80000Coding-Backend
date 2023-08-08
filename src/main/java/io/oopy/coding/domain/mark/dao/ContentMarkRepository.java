package io.oopy.coding.domain.mark.dao;

import io.oopy.coding.domain.mark.entity.ContentMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentMarkRepository extends JpaRepository<ContentMark, Long> {

    @Query("select cm from ContentMark cm join fetch cm.content c where c.id = :contentId")
    List<ContentMark> findContentMarksByContentId(@Param("contentId") Long contentId);
}
