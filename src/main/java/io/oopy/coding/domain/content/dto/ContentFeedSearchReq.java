package io.oopy.coding.domain.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentFeedSearchReq {

    @Builder
    @AllArgsConstructor
    @Getter
    @Schema(description = "게시글 피드 검색 - 제목으로")
    public static class Title {
        private String title;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    @Schema(description = "게시글 피드 검색 - 내용으로")
    public static class Body {
        private String body;
    }
}
