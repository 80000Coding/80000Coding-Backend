package io.oopy.coding.api.mark.controller;

import io.oopy.coding.api.mark.service.ContentMarkService;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.mark.dto.ChangeUserPressReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "게시글 마크", description = "게시글 마크 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/contents/{content_id}/marks")
public class ContentMarkController {

    private final ContentMarkService contentMarkService;

    @Operation(summary = "게시글 전체 mark 개수", description = "특정 게시글의 각 mark(like, bookmark) 의 전체 개수를 반환")
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getContentMark(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentMarkService.getMarkByContent(contentId)));
    }

    @Operation(summary = "게시글 유저 개인 press 여부", description = "현재 로그인 되어 있는 유저가 특성 게시글의 mark 를 눌렀는지에 대한 여부 판단")
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserPress(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId,
                                          @AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentMarkService.getUserPress(securityUser, contentId)));
    }

    @Operation(summary = "게시글 유저 개인 press 여부 토글", description = "현재 로그인되어 있는 유저가 특정 게시글의 mark 를 눌렀을 때 토글하는 부분")
    @PatchMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> pressMark(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId,
                                       @AuthenticationPrincipal CustomUserDetails securityUser,
                                       @Valid @RequestBody ChangeUserPressReq req) {
        contentMarkService.changeUserPress(contentId, securityUser, req);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }
}