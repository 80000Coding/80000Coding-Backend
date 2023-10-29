package io.oopy.coding.domain.content.dto;


import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetContentRes {
    private ResContent content;
    private ResUser user;
    private ContentCategory contentCategory;

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
        private boolean complete;
        private String contentImageUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;

        public static ResContent from(Content content) {
            return ResContent.builder()
                    .id(content.getId())
                    .title(content.getTitle())
                    .body(content.getBody())
                    .type(content.getType())
                    .views(content.getViews())
                    .repoName(content.getRepoName())
                    .repoOwner(content.getRepoOwner())
                    .complete(content.isComplete())
                    .contentImageUrl(content.getContentImageUrl())
                    .createdAt(content.getCreatedAt())
                    .updatedAt(content.getUpdatedAt())
                    .deletedAt(content.getDeleteAt())
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ResUser {
        private String name;
        private String profileImageUrl;

        public static ResUser from(User user) {
            return new ResUser(user.getName(), user.getProfileImageUrl());
        }
    }


    public static class ContentCategory {
        private Long contentCategoryId;
    }

    // TODO contentCategory 추가 시 변경 예정
    public static GetContentRes from(Content content) {
        return new GetContentRes(
                ResContent.from(content),
                ResUser.from(content.getUser()),
                null);
    }

}
