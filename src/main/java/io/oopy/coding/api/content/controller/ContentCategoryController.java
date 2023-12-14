package io.oopy.coding.api.content.controller;

import io.oopy.coding.api.content.service.ContentCategoryService;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.content.dto.ChangeCategoryReq;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class ContentCategoryController {
    private final ContentCategoryService contentCategoryService;

    @Operation(summary = "게시글 관련된 모든 카테고리 불러오기")
    @GetMapping("/get")
    public ResponseEntity<?> getCategories(@RequestParam Long contentId) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentCategoryService.getCategories(contentId)));
    }

    @Operation(summary = "게시글 카테고리 설정")
    @PatchMapping("/add")
    public ResponseEntity<?> addCategory(@Valid @RequestBody ChangeCategoryReq req, @AuthenticationPrincipal CustomUserDetails securityUser) {
        contentCategoryService.addCategory(req, securityUser);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }

    @Operation(summary = "게시글 카테고리 해제")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(@Valid @RequestBody ChangeCategoryReq req, @AuthenticationPrincipal CustomUserDetails securityUser) {
        contentCategoryService.deleteCategory(req, securityUser);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }
}
