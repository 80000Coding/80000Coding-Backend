package io.oopy.coding.api.mark.controller;

import io.oopy.coding.api.mark.service.ContentMarkService;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.mark.dto.ChangeUserPressReq;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mark")
public class ContentMarkController {

    private final ContentMarkService contentMarkService;

    @Operation(summary = "게시글 전체 mark 개수")
    @GetMapping("/get")
    public ResponseEntity<?> getContentMark(@RequestParam Long contentId) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentMarkService.getMarkByContent(contentId)));
    }

    @Operation(summary = "게시글 유저 개인 press 여부")
    @PostMapping("")
    public ResponseEntity<?> getUserPress(@AuthenticationPrincipal CustomUserDetails securityUser, @RequestParam Long contentId) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentMarkService.getUserPress(securityUser, contentId)));
    }

    @Operation(summary = "게시글 유저 개인 press 여부 토글")
    @PatchMapping("")
    public ResponseEntity<?> pressMark(@AuthenticationPrincipal CustomUserDetails securityUser, @Valid @RequestBody ChangeUserPressReq req) {
        contentMarkService.changeUserPress(securityUser, req);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }
}