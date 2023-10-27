package io.oopy.coding.content.controller;

import io.oopy.coding.domain.content.dto.*;
import io.oopy.coding.content.service.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {

//    private final GetContent getContentService;
//    private final CreateContent createContentService;
//    private final UpdateContent updateContentService;
//    private final DeleteContent deleteContentService;
    private final ContentService contentService;
    private final ContentSearchService contentSearchService;


    @Operation(summary = "게시글 상세 페이지", description = "Request로 content_id")
    @GetMapping("")
    public ResponseEntity<?> GetContent(@RequestParam Long contentId) {
        GetContentRes response = contentService.getContent(contentId);

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "게시글 생성", description = "Request로 user_id, type, repo_name, repo_owner")
    @PostMapping("")
    public ResponseEntity<?> createContent(@RequestBody CreateContentReq request) {
        return ResponseEntity.ok().body(contentService.createContent(request));
    }

    @Operation(summary = "게시글 수정", description = "Request로 content_id, title, body")
    @PatchMapping("")
    public ResponseEntity<?> updateContent(@RequestBody UpdateContentReq request) {
        return ResponseEntity.ok().body(contentService.updateContent(request));
    }

    @Operation(summary = "게시글 삭제(Soft Delete)", description = "Request로 content_id")
    @DeleteMapping("")
    public ResponseEntity<?> deleteContent(@RequestParam Long contentId) {
        return ResponseEntity.ok().body(contentService.deleteContent(contentId));
    }

    @Operation(summary = "유저 게시글 조회", description = "Request로 user_id")
    @GetMapping("/user")
    public ResponseEntity<?> getUserContents(@RequestParam Long userId) {
        return ResponseEntity.ok().body(contentSearchService.getUserContents(userId));
    }
}
