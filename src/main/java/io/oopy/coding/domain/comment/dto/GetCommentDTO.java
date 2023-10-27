package io.oopy.coding.domain.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class GetCommentDTO {

    private Comment comment;
    private User user;

    @Builder
    @Getter
    public static class Comment {
        private String body;
        private Long parent_id;
        private LocalDateTime created_at;
        private LocalDateTime updated_at;
        private LocalDateTime deleted_at;
    }

    @Builder
    @Getter
    public static class User {
        private String name;
        private String profile_image_url;
    }
}
