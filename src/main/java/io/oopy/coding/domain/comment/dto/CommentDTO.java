package io.oopy.coding.domain.comment.dto;

import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentDTO {
    private Long id;
    private String commentBody;
    private Long parentId;
    private LocalDateTime deleteAt;

    public static CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .commentBody(comment.getCommentBody())
                .parentId(comment.getParentId())
                .deleteAt(comment.getDeleteAt())
                .build();
    }
}
