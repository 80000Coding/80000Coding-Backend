package io.oopy.coding.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class UpdateCommentDTO {

    @Builder
    @Getter
    public static class Req {
        private Long user_id;
        private Long comment_id;
        private String content;
    }

    @Builder
    @Getter
    public static class Res {
        private String status;
        private Object data;
        private String message;

        @Builder
        @Getter
        public static class Data {
            private Long comment_id;
            private LocalDateTime created_at;
            private LocalDateTime updated_at;
            private LocalDateTime deleted_at;
        }

        @Builder
        @Getter
        public static class DeletedComment {
            private Long comment_id;
            private LocalDateTime deleted_at;
        }
    }
}
