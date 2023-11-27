package io.oopy.coding.api.comment.service;

import io.oopy.coding.api.comment.exception.CommentErrorCode;
import io.oopy.coding.api.comment.exception.CommentErrorException;
import io.oopy.coding.api.content.exception.ContentErrorCode;
import io.oopy.coding.api.content.exception.ContentErrorException;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.comment.dto.*;
import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.domain.comment.repository.CommentRepository;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.domain.user.repository.UserRepository;
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
    public SuccessResponse getComments(Long contentId) {
        contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID));

        List<Comment> comments = commentRepository.findCommentsByContentId(contentId);

        List<CommentDTO> response = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDTO dto = CommentDTO.fromEntity(comment);
            response.add(dto);
        }

        return SuccessResponse.from(response);
    }

    /**
     * 댓글 생성
     * @param req
     */
    public SuccessResponse createComment(CreateCommentReq req, CustomUserDetails securityUser) {
        User user = userRepository.findById(securityUser.getUserId()).orElse(null);

        Content content = contentRepository.findById(req.getContentId())
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID));

        if (req.getParentId() != null) {
            Comment parentComment = commentRepository.findById(req.getParentId())
                    .orElseThrow(() -> new CommentErrorException(CommentErrorCode.INVALID_PARENT_COMMENT));
            if (parentComment.getDeleteAt() != null) {
                throw new CommentErrorException(CommentErrorCode.INVALID_PARENT_COMMENT);
            }
        }

        Comment newComment = Comment.builder()
                .content(content)
                .user(user)
                .commentBody(req.getContent())
                .parentId(req.getParentId())
                .build();

        commentRepository.save(newComment);

        return SuccessResponse.from(CreateCommentRes.from(req, newComment));
    }

    /**
     * 댓글 삭제 (soft delete)
     * @param commentId
     */
    @Transactional
    public SuccessResponse deleteComment(Long commentId, CustomUserDetails securityUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentErrorException(CommentErrorCode.INVALID_COMMENT_ID));

        if (comment.getDeleteAt() != null)
            throw new CommentErrorException(CommentErrorCode.DELETED_COMMENT);

        if (securityUser.getRole().equals("ROLE_ADMIN") || comment.getUser().getId().equals(securityUser.getUserId()))
            comment.deleteComment();
        else
            throw new CommentErrorException(CommentErrorCode.REQUEST_USER_DATA_OWNER_MISMATCH);

        return SuccessResponse.from(DeleteCommentRes.of(commentId, comment.getDeleteAt()));
    }

    /**
     * 댓글 수정
     * @param req
     */
    public SuccessResponse updateComment(UpdateCommentReq req, CustomUserDetails securityUser) {
        Comment comment = commentRepository.findById(req.getCommentId())
                .orElseThrow(() -> new CommentErrorException(CommentErrorCode.INVALID_COMMENT_ID));

        if (comment.getDeleteAt() != null)
            throw new CommentErrorException(CommentErrorCode.DELETED_COMMENT);

        if (securityUser.getRole().equals("ROLE_ADMIN") || comment.getUser().getId().equals(securityUser.getUserId()))
            commentRepository.save(comment.updateComment(req.getContent()));
        else
            throw new CommentErrorException(CommentErrorCode.REQUEST_USER_DATA_OWNER_MISMATCH);

        return SuccessResponse.from(UpdateCommentRes.of(comment.getId(), comment.getUpdatedAt()));
    }
}
