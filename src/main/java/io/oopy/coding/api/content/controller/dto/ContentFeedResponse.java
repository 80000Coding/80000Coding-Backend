package io.oopy.coding.api.content.controller.dto;

import io.oopy.coding.common.types.CategoryType;
import io.oopy.coding.common.types.HashTagType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentFeedResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ContentFeedList {
        private String contentId;
        private String contentImageUrl;
        private String title;
        private String body;
        private List<CategoryType> categoryTagList;
        private List<HashTagType> hashTagList;
        private String profileImageUrl;
        private Integer userId;
        private String userName;
        private LocalDateTime createDt;
        private Integer views;
        private Integer commentCount;
        private Integer markCount;
        private Integer likeCount;
    }
}
