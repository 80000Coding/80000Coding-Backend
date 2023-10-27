package io.oopy.coding.domain.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class CreateCommentDTO {
    private Long content_id;
    private Long comment_id;
    private String comment_body;
    private Long parent_id;
    private LocalDateTime created_at;
}
