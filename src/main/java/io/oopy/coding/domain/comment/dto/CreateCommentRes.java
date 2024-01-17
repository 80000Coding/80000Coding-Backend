package io.oopy.coding.domain.comment.dto;

import io.oopy.coding.domain.comment.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CreateCommentRes {
    private Long contentId;
    private Long commentId;
    private String commentBody;
    private Long parentId;
    private LocalDateTime createdAt;

    public static CreateCommentRes from(Long contentId, CreateCommentReq req, Comment comment) {
        return CreateCommentRes.builder()
                .contentId(contentId)
                .commentId(comment.getId())
                .commentBody(req.getContent())
                .parentId(req.getParentId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
