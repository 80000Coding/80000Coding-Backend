package io.oopy.coding.api.content.controller;

import io.oopy.coding.api.content.controller.dto.ContentFeedResponse;
import io.oopy.coding.api.content.service.ContentFeedService;
import io.oopy.coding.common.types.CategoryType;
import io.oopy.coding.common.types.HashTagType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "게시글 피드", description = "게시글 피드 API")
@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class ContentFeedController {
    private final ContentFeedService contentFeedService;

    @Operation(summary = "게시글 피드 리스트", description = "게시글 피드 리스트")
    @GetMapping("")
    public ContentFeedResponse.ContentFeedList ContentFeedList() {
        return ContentFeedResponse.ContentFeedList.builder()
                .contentId("1")
                .contentImageUrl("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg")
                .title("테스트 제목")
                .body("테스트 본문")
                .categoryTagList(List.of(CategoryType.builder().title("테스트").build()))
                .hashTagList(List.of(HashTagType.builder().title("테스트").build()))
                .profileImageUrl("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg")
                .userId(1)
                .userName("테스트 유저")
                .createDt(LocalDateTime.now())
                .views(1)
                .commentCount(1)
                .markCount(1)
                .likeCount(1)
                .build();
    }
}
