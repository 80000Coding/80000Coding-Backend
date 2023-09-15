package io.oopy.coding.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class CreateCommentDTO {

    @Builder
    @Getter
    public static class Req {
        private Long user_id;
        private Long content_id;
        private String content;
        private Long parent_id;
    }

    @Builder
    @Getter
    public static class Res {
        private String status;
        private Object data;
        private String message;

        @Builder
        @Getter
        public static class successResponse {
            private Long comment_id;
            private Long content_id;
            private String comment_body;
            private Long parent_id;
            private LocalDateTime created_at;
            private LocalDateTime updated_at;
            private LocalDateTime deleted_at;
        }

        @Builder
        @Getter
        public static class DeletedContent {
            private Long  content_id;
            private LocalDateTime deleted_at;
        }
    }
}
