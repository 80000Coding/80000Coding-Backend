package io.oopy.coding.api.content.controller;

import io.oopy.coding.api.content.service.ContentCategoryService;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.content.dto.ChangeCategoryReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contents/{content_id}/categories")
@RequiredArgsConstructor
public class ContentCategoryController {
    private final ContentCategoryService contentCategoryService;

    @Operation(summary = "게시글 관련된 모든 카테고리 불러오기", description = "특정 게시글에 등록되어 있는 카테고리 목록 반환")
    @GetMapping("")
    public ResponseEntity<?> getCategories(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentCategoryService.getCategories(contentId)));
    }

    @Operation(summary = "게시글 카테고리 설정", description = "특정 게시글에 카테고리 설정 추가")
    @PatchMapping("/add")
    public ResponseEntity<?> addCategory(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId,
                                         @Valid @RequestBody ChangeCategoryReq req) {
        contentCategoryService.addCategory(contentId, req);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }

    @Operation(summary = "게시글 카테고리 해제", description = "특정 게시글에 설정되어 있는 카테고리 해제")
    @DeleteMapping("/unselect")
    public ResponseEntity<?> deleteCategory(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId,
                                            @Valid @RequestBody ChangeCategoryReq req) {
        contentCategoryService.deleteCategory(contentId, req);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }
}
