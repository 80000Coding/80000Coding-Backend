package io.oopy.coding.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class GetCommentDTO {

    @Builder
    @Getter
    public static class Req {
        private Long contentId;
    }

    @Builder
    @Getter
    public static class Res {
        private String status;
        private String message;
        private Object data;

        @Builder
        @Getter
        public static class Data {
            private Comment comment;
            private User user;
        }

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

        @Builder
        @Getter
        public static class ContentEmpty {
            private Long content_id;
        }

        @Builder
        @Getter
        public static class DeletedContent {
            private Long content_id;
            private LocalDateTime deleted_at;
        }
    }
}
