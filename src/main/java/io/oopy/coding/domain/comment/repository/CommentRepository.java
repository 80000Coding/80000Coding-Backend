package io.oopy.coding.domain.comment.repository;

import io.oopy.coding.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByContentId(Long contentId);

    boolean existsByIdAndUserId(Long commentId, Long userId);

    @Query("select count(c) from Comment c where c.content.id = :contentId")
    Long countByContentId(Long contentId);
}
