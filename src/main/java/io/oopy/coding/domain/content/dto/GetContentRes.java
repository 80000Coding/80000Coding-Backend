package io.oopy.coding.domain.content.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class GetContentRes {
    private Content content;
    private User user;
    private ContentCategory contentCategory;

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


    public static class ContentCategory {
        private Long contentCategory_id;
    }

}
