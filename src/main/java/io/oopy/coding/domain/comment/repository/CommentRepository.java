package io.oopy.coding.domain.comment.repository;

import io.oopy.coding.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select cmt from Comment cmt join fetch cmt.content c where c.id = :contentId")
    List<Comment> findCommentsByContentId(@Param("contentId") Long contentId);

    @Modifying(clearAutomatically = true)
    @Query("update Comment c set c.commentBody = :commentBody where c.id = :commentId")
    int updateCommentBody(Long commentId, String commentBody);

    @Modifying(clearAutomatically = true)
    @Query("delete from Comment c where c.id = :commentId")
    int deleteCommentById(@Param("commentId") Long commentId);
}
