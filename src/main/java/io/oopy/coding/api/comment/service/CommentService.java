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
import io.oopy.coding.domain.user.entity.RoleType;
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
    public List<GetCommentRes> getComments(Long contentId) {
        contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID));

        List<Comment> comments = commentRepository.findCommentsByContentId(contentId);

        List<GetCommentRes> response = new ArrayList<>();

        for (Comment comment : comments) {
            GetCommentRes dto = GetCommentRes.fromEntity(comment);
            response.add(dto);
        }

        return response;
    }

    /**
     * 댓글 생성
     * @param req
     */
    public CreateCommentRes createComment(Long contentId, CreateCommentReq req, CustomUserDetails securityUser) {
        User user = userRepository.findById(securityUser.getUserId()).orElse(null);

        Content content = contentRepository.findById(contentId)
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

        return CreateCommentRes.from(contentId, req, newComment);
    }

    /**
     * 댓글 수정
     * @param req
     */
    public UpdateCommentRes updateComment(Long contentId, Long commentId, UpdateCommentReq req, CustomUserDetails securityUser) {
        if (!contentRepository.existsByIdAndDeleteAtIsNull(contentId)) {
            throw new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID);
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentErrorException(CommentErrorCode.INVALID_COMMENT_ID));

        if (comment.getDeleteAt() != null)
            throw new CommentErrorException(CommentErrorCode.DELETED_COMMENT);

        commentRepository.save(comment.updateComment(req.getContent()));

        return UpdateCommentRes.of(comment.getId(), comment.getUpdatedAt());
    }

    /**
     * 댓글 삭제 (soft delete)
     * @param commentId
     */
    @Transactional
    public DeleteCommentRes deleteComment(Long contentId, Long commentId, CustomUserDetails securityUser) {
        System.out.println("repository result = " + contentRepository.existsByIdAndDeleteAtIsNull(contentId));
        if (!contentRepository.existsByIdAndDeleteAtIsNull(contentId)) {
            throw new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID);
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentErrorException(CommentErrorCode.INVALID_COMMENT_ID));

        if (comment.getDeleteAt() != null)
            throw new CommentErrorException(CommentErrorCode.DELETED_COMMENT);

        comment.deleteComment();

        return DeleteCommentRes.of(commentId, comment.getDeleteAt());
    }

}
