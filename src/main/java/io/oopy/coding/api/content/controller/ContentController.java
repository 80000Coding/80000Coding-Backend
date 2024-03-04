package io.oopy.coding.api.content.controller;

import io.oopy.coding.api.content.service.*;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.common.security.authorization.AuthorManager;
import io.oopy.coding.domain.content.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "게시글", description = "게시글 관련 API(피드X)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contents")
public class ContentController {

    private final ContentService contentService;

    @Operation(summary = "게시글 1개 상세조회", description = "게시글 중 한 개를 선택하여 상세 내용을 가져오는 API")
    @GetMapping("/{content_id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getContent(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentService.getContent(contentId)));
    }

    @Operation(summary = "게시글 생성", description = "게시글 생성 버튼을 누르는 즉시 content 목록 생성. 이후 작성되는 내용은 PATCH 메소드를 통한다(임시저장 기능을 위해)")
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createContent(@Valid @RequestBody CreateContentReq req,
                                           @AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentService.createContent(req, securityUser)));
    }

    @Operation(summary = "게시글 1개 수정 및 작성완료", description = "publish = true 일 경우 생성된 게시물, true 가 아닐 경우 임시저장 된 게시물로 판단")
    @PatchMapping("/{content_id}")
    @PreAuthorize("isAuthenticated() && @authorManager.isContentAuthor(authentication.getPrincipal(), #contentId)")
    public ResponseEntity<?> updateContent(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId,
                                           @Valid @RequestBody UpdateContentReq req,
                                           @AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentService.updateContent(contentId, req, securityUser)));
    }

    @Operation(summary = "게시글 1개 삭제", description = "게시글 삭제 기능. Soft Delete 를 이용하여 Deleted_dt값 세팅")
    @DeleteMapping("/{content_id}")
    @PreAuthorize("isAuthenticated() && @authorManager.isContentAuthor(authentication.getPrincipal(), #contentId)")
    public ResponseEntity<?> deleteContent(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId,
                                           @AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentService.deleteContent(contentId, securityUser)));
    }

    @Operation(summary = "유저가 작성한 게시글", description = "AT로 들어온 user_id에 해당하는 유저가 작성한 게시글 리턴")
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserContents(@AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentService.getUserContents(securityUser)));
    }
}
