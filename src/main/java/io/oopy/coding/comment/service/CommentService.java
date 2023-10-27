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
    public List<CommentDTO> getComments(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content does not exist"));

        List<Comment> comments = commentRepository.findCommentsByContentId(contentId);
        List<CommentDTO> response = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDTO dto = CommentDTO.toDTO(comment);
            response.add(dto);
        }

        return response;
    }

    /**
     * 댓글 생성
     * @param req
     */
    public CreateCommentDTO createComment(CreateCommentReq req) {
        User user = userRepository.findById(req.getUser_id())
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        Content content = contentRepository.findById(req.getContent_id())
                .orElseThrow(() -> new EntityNotFoundException("Content does not exist"));

        Comment newComment = Comment.builder()
                .content(content)
                .user(user)
                .commentBody(req.getContent())
                .parentId(req.getParent_id())
                .build();

        commentRepository.save(newComment);

        CreateCommentDTO response = CreateCommentDTO.builder()
                .content_id(req.getContent_id())
                .comment_id(newComment.getId())
                .comment_body(req.getContent())
                .parent_id(req.getParent_id())
                .created_at(newComment.getCreatedAt())
                .build();

        return response;
    }

    /**
     * 댓글 삭제 (soft delete)
     * @param commentId
     */
    public DeleteCommentDTO deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment does not exist"));

        comment.deleteComment();
        commentRepository.save(comment);

        return DeleteCommentDTO.builder()
                .comment_id(commentId)
                .deleted_at(comment.getDeleteAt())
                .build();
    }

    /**
     * 댓글 수정
     * @param req
     */
    public UpdateCommentDTO updateComment(UpdateCommentReq req) {
        Comment comment = commentRepository.findById(req.getComment_id())
                .orElseThrow(() -> new EntityNotFoundException("Comment does not exist"));

        comment.updateComment(req.getContent());
        commentRepository.save(comment);

        return UpdateCommentDTO.builder()
                .comment_id(req.getComment_id())
                .updated_at(comment.getUpdatedAt())
                .build();
    }
}
