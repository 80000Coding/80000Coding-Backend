package io.oopy.coding.domain.content.dto;


import io.oopy.coding.domain.content.entity.Category;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.entity.ContentCategory;
import io.oopy.coding.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class GetContentRes {
    private ResContent content;
    private ResUser user;
    private ResContentCategory contentCategory;

    @Builder
    @Getter
    public static class ResContent {
        private Long id;
        private String title;
        private String body;
        private String type;
        private Long views;
        private String repoName;
        private String repoOwner;
        private Boolean publish;
        private String contentImageUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;

        public static ResContent from(Content content) {
            return ResContent.builder()
                    .id(content.getId())
                    .title(content.getTitle())
                    .body(content.getBody())
                    .type(content.getType().getType())
                    .views(content.getViews())
                    .repoName(content.getRepoName())
                    .repoOwner(content.getRepoOwner())
                    .publish(content.getPublish())
                    .contentImageUrl(content.getContentImageUrl())
                    .createdAt(content.getCreatedAt())
                    .updatedAt(content.getUpdatedAt())
                    .deletedAt(content.getDeleteAt())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ResUser {
        private String name;
        private String profileImageUrl;

        public static ResUser from(User user) {
            return new ResUser(user.getName(), user.getProfileImageUrl());
        }
    }


    @Builder
    @Getter
    @AllArgsConstructor
    public static class ResContentCategory {
        @Builder
        @Getter
        @AllArgsConstructor
        public static class CategoryAttributes {
            private String type;
            private String color;

            public static CategoryAttributes from(Category category) {
                return CategoryAttributes.builder()
                        .type(category.getName())
                        .color(category.getColor())
                        .build();
            }
        }

        private List<CategoryAttributes> contentCategory;

        public static ResContentCategory from(Content content) {
            List<CategoryAttributes> contentCategory = content.getContentCategories().stream()
                    .map(mappedContentCategory -> CategoryAttributes.from(mappedContentCategory.getCategory()))
                    .collect(Collectors.toList());

            return ResContentCategory.builder()
                    .contentCategory(contentCategory)
                    .build();
        }
    }

    public static GetContentRes from(Content content) {
        return new GetContentRes(
                ResContent.from(content),
                ResUser.from(content.getUser()),
                ResContentCategory.from(content));
    }

}
