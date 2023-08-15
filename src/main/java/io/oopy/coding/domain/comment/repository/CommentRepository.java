package io.oopy.coding.domain.comment.repository;

import io.oopy.coding.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select cmt from Comment cmt join fetch cmt.content c where c.id = :contentId")
    List<Comment> findCommentsByContentId(@Param("contentId") Long contentId);
}
