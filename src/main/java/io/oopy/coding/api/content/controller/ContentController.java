package io.oopy.coding.api.content.controller;

import io.oopy.coding.api.content.service.*;
import io.oopy.coding.common.resolver.access.AccessToken;
import io.oopy.coding.common.resolver.access.AccessTokenInfo;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.content.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contents")
public class ContentController {

    private final ContentService contentService;


    // TODO 비회원/회원 이용은 Get만 진행, 뒤쪽 url을 구분해서 ignoring에 작성해야할 것 같음(url 이름 논의)
    @GetMapping("/get")
    public ResponseEntity<?> GetContent(@RequestParam Long contentId) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentService.getContent(contentId)));
    }

    @PostMapping("/post")
    public ResponseEntity<?> createContent(@Valid @RequestBody CreateContentReq req, @AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentService.createContent(req, securityUser)));
    }

    @Valid
    @PatchMapping("/update")
    public ResponseEntity<?> updateContent(@Valid @RequestBody UpdateContentReq req, @AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentService.updateContent(req, securityUser)));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteContent(@RequestParam Long contentId, @AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentService.deleteContent(contentId, securityUser)));
    }
}
