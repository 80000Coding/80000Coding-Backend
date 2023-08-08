package io.oopy.coding.domain.comment.dao;

import io.oopy.coding.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select cmt from Comment cmt join fetch cmt.content c where c.id = :contentId")
    List<Comment> findCommentsByContentId(@Param("contentId") Long contentId);
}
