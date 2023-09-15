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
public class CreateContentDTO {

    @Builder
    @Getter
    public static class Req {
        private Long user_id;
        private String type;
        private String repo_name;
        private String repo_owner;
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
            private Long content_id;
            private LocalDateTime created_at;
            private LocalDateTime updated_at;
            private LocalDateTime deleted_at;
        }

        @Builder
        @Getter
        public static class UserEmpty {
            private Long user_id;
        }
    }
}
