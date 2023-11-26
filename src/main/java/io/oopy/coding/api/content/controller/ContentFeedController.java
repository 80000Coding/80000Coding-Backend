package io.oopy.coding.api.content.controller;

import io.oopy.coding.api.content.controller.dto.ContentFeedResponse;
import io.oopy.coding.api.content.service.ContentFeedService;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.types.CategoryType;
import io.oopy.coding.common.types.HashTagType;
import io.oopy.coding.domain.content.dto.ContentDTO;
import io.oopy.coding.domain.content.dto.ContentFeedSearchReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "게시글 피드", description = "게시글 피드 API")
@RestController
@RequestMapping("/api/v1/feed")
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

    @Operation(summary = "게시글 제목으로 검색", description = "게시글 제목으로 검색")
    @GetMapping("/title")
    public ResponseEntity<SuccessResponse<List<ContentDTO> > > contentFeedListByTitle(ContentFeedSearchReq.Title contentFeedSearchReq, @PageableDefault Pageable pageable) {
        Page<ContentDTO> contentDtos = contentFeedService.searchByTitle(contentFeedSearchReq, pageable);
        return ResponseEntity.ok(SuccessResponse.of(contentDtos.getContent(), contentDtos.getTotalPages(), contentDtos.getTotalElements()));
    }

    @Operation(summary = "게시글 내용으로 검색", description = "게시글 내용으로 검색")
    @GetMapping("/body")
    public ResponseEntity<SuccessResponse<List<ContentDTO> > > contentFeedListByBody(ContentFeedSearchReq.Body contentFeedSearchReq, @PageableDefault Pageable pageable) {
        Page<ContentDTO> contentDtos = contentFeedService.searchByBody(contentFeedSearchReq, pageable);
        return ResponseEntity.ok(SuccessResponse.of(contentDtos.getContent(), contentDtos.getTotalPages(), contentDtos.getTotalElements()));
    }
}