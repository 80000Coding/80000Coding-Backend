package io.oopy.coding.domain.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class UpdateCommentDTO {
    private Long comment_id;
    private LocalDateTime updated_at;
}
