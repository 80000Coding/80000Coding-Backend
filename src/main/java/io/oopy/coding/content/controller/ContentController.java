package io.oopy.coding.content.controller;

import io.oopy.coding.domain.content.dto.*;
import io.oopy.coding.content.service.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contents")
// TODO 유저관련 검증부분 추가(Token, sofDelete 된 유저 등..)
public class ContentController {

    private final ContentService contentService;
    private final ContentSearchService contentSearchService;

    @GetMapping("")
    public ResponseEntity<?> GetContent(@RequestParam Long contentId) {
        GetContentRes response = contentService.getContent(contentId);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("")
    public ResponseEntity<?> createContent(@Valid @RequestBody CreateContentReq request) {
        return ResponseEntity.ok().body(contentService.createContent(request));
    }

    @Valid
    @PatchMapping("")
    public ResponseEntity<?> updateContent(@Valid @RequestBody UpdateContentReq request) {
        return ResponseEntity.ok().body(contentService.updateContent(request));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteContent(@RequestParam Long contentId) {
        return ResponseEntity.ok().body(contentService.deleteContent(contentId));
    }

    // 아직 이야기 된 부분은 없이 혼자 필요하지 않을까 해서 만들어 놓은 기능
    @GetMapping("/user")
    public ResponseEntity<?> getUserContents(@RequestParam Long userId) {
        return ResponseEntity.ok().body(contentSearchService.getUserContents(userId));
    }
}
