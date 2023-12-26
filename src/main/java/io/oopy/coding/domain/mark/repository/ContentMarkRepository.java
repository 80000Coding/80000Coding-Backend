package io.oopy.coding.domain.mark.repository;

import io.oopy.coding.domain.mark.entity.MarkType;
import io.oopy.coding.domain.mark.dto.CountMark;
import io.oopy.coding.domain.mark.entity.ContentMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentMarkRepository extends JpaRepository<ContentMark, Long> {

    @Query("select " +
            "sum(case when cm.type = '1' then 1 else 0 end) as like, " +
            "sum(case when cm.type = '2' then 1 else 0 end) as bookmark " +
            "from ContentMark cm " +
            "where cm.content.id = :contentId")
    List<CountMark> getContentMarkCountsByContentId(@Param("contentId") Long contentId);

    List<ContentMark> findContentMarksByContentIdAndUserId(Long contentId, Long userId);

    ContentMark findContentMarksByContentIdAndUserIdAndType(Long contentId, Long userId, MarkType type);
}
