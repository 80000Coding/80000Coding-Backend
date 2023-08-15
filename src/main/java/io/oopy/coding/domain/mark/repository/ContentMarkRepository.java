package io.oopy.coding.domain.mark.repository;

import io.oopy.coding.domain.mark.entity.ContentMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentMarkRepository extends JpaRepository<ContentMark, Long> {

    @Query("select cm from ContentMark cm join fetch cm.content c where c.id = :contentId")
    List<ContentMark> findContentMarksByContentId(@Param("contentId") Long contentId);

    @Query("select cm from ContentMark cm where cm.content.id = :contentId and cm.user.id = :userId")
    List<ContentMark> findContentMarkPressByContentIdAndUserId(@Param("contentId") Long contentid, @Param("userId") Long userId);
}
