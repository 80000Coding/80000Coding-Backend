package io.oopy.coding.domain.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DeleteCommentRes {
    private Long commentId;
    private LocalDateTime deletedAt;

    public static DeleteCommentRes of(Long commentId, LocalDateTime deletedAt) {
        return new DeleteCommentRes(commentId, deletedAt);
    }
}
