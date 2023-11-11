package io.oopy.coding.domain.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UpdateCommentRes {
    private Long commentId;
    private LocalDateTime updatedAt;

    public static UpdateCommentRes of(Long commentId, LocalDateTime updatedAt) {
        return new UpdateCommentRes(commentId, updatedAt);
    }
}
