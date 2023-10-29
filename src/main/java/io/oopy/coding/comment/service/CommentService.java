package io.oopy.coding.comment.service;

import io.oopy.coding.domain.comment.dto.*;
import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.domain.comment.repository.CommentRepository;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.entity.User;
import io.oopy.coding.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 정보
     * @param contentId
     */
    @Transactional
    public List<CommentDTO> getComments(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content does not exist"));

        List<Comment> comments = commentRepository.findCommentsByContentId(contentId);
        List<CommentDTO> response = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDTO dto = CommentDTO.fromEntity(comment);
            response.add(dto);
        }

        return response;
    }

    /**
     * 댓글 생성
     * @param req
     */
    public CreateCommentRes createComment(CreateCommentReq req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        Content content = contentRepository.findById(req.getContentId())
                .orElseThrow(() -> new EntityNotFoundException("Content does not exist"));

        Comment newComment = Comment.builder()
                .content(content)
                .user(user)
                .commentBody(req.getContent())
                .parentId(req.getParentId())
                .build();

        commentRepository.save(newComment);

        return CreateCommentRes.from(req, newComment);
    }

    /**
     * 댓글 삭제 (soft delete)
     * @param commentId
     */
    @Transactional
    public DeleteCommentRes deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment does not exist"));

        comment.deleteComment();

        return DeleteCommentRes.of(commentId, comment.getDeleteAt());
    }

    /**
     * 댓글 수정
     * @param req
     */
    public UpdateCommentRes updateComment(UpdateCommentReq req) {
        Comment comment = commentRepository.findById(req.getCommentId())
                .orElseThrow(() -> new EntityNotFoundException("Comment does not exist"));

        commentRepository.save(comment.updateComment(req.getContent()));

        return UpdateCommentRes.of(comment.getId(), comment.getUpdatedAt());
    }
}
