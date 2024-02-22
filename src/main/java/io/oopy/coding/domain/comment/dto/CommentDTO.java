package io.oopy.coding.domain.comment.dto;

import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CommentDTO {
    private Long id;
    private String commentBody;
    private Long parentId;
    private LocalDateTime deletedAt;

    public static CommentDTO fromEntity(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .commentBody(comment.getCommentBody())
                .parentId(comment.getParentId())
                .deletedAt(comment.getDeleteAt())
                .build();
    }
}
