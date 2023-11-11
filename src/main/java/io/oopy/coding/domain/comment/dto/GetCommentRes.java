package io.oopy.coding.domain.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class GetCommentRes {

    private Comment comment;
    private User user;

    @Builder
    @Getter
    public static class Comment {
        private String body;
        private Long parentId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;
    }

    @Builder
    @Getter
    public static class User {
        private String name;
        private String profileImageUrl;
    }
}
