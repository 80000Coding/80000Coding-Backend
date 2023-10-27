package io.oopy.coding.domain.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class DeleteCommentDTO {
    private Long comment_id;
    private LocalDateTime deleted_at;
}
