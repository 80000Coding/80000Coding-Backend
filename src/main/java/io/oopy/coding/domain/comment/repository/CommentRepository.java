package io.oopy.coding.domain.comment.repository;

import io.oopy.coding.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select cmt from Comment cmt join fetch cmt.content c where c.id = :contentId")
    List<Comment> findCommentsByContentId(@Param("contentId") Long contentId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Comment c set c.commentBody = :commentBody, c.updatedAt = current_timestamp where c.id = :commentId")
    int updateCommentBody(@Param("commentId") Long commentId, @Param("commentBody") String commentBody);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Comment c set c.deleteAt = current_timestamp, c.commentBody = '' where c.id = :commentId")
    int softDeleteCommentById(@Param("commentId") Long commentId);

}
