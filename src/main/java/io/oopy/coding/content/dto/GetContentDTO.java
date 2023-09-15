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
public class GetContentDTO {

    @Builder
    @Getter
    public static class Req {
        private Long content_id;
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
            private Content content;
            private User user;
            private ContentCategory contentCategory;
            private Category category;
        }

        @Builder
        @Getter
        public static class Content {
            private Long id;
            private String title;
            private String body;
            private String type;
            private Long views;
            private String repo_name;
            private String repo_owner;
            private boolean complete;
            private String content_image_url;
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
        public static class ContentCategory {
            private Long category_id;
        }

        @Builder
        @Getter
        public static class Category {
            private String name;
            private String color;
        }

        @Builder
        @Getter
        public static class ContentEmpty {
            private Long content_id;
        }

        @Builder
        @Getter
        public static class UserEmpty {
            private Long user_id;
        }

        @Builder
        @Getter
        public static class CategoryEmpty {
            private Long category_id;
        }

        @Builder
        @Getter
        public static class ContentCategoryEmpty {
            private Long contentCategory_id;
        }

        @Builder
        @Getter
        public static class DeletedContent{
            private Long content_id;
            private LocalDateTime deleted_at;
        }
    }
}
