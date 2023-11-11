package io.oopy.coding.domain.comment.repository;

import io.oopy.coding.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByContentId(Long contentId);

}
