package io.oopy.coding.content.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class DeleteContentDTO {

    @Getter
    @Builder
    public static class Req {
        private Long contentId;
    }

    @Getter
    @Builder
    public static class Res {
        private String status;
        private Object data;
        private String message;

        @Builder
        @Getter
        public static class Data {
            private Long content_id;
            private LocalDateTime created_at;
            private LocalDateTime updated_at;
            private LocalDateTime deleted_at;
        }

        @Builder
        @Getter
        public static class ContentEmpty {
            private Long content_id;
        }

        @Builder
        @Getter
        public static class CommentEmpty {
            private Long content_id;
        }
    }
}
