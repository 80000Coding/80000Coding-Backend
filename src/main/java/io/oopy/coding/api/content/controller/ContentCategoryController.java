package io.oopy.coding.api.content.controller;

import io.oopy.coding.api.content.service.ContentCategoryService;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.domain.content.dto.ChangeCategoryReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class ContentCategoryController {
    private final ContentCategoryService contentCategoryService;

    @GetMapping("")
    public ResponseEntity<?> getCategories(@RequestParam Long contentId) {
        return ResponseEntity.ok().body(SuccessResponse.from(contentCategoryService.getCategories(contentId)));
    }

    @PatchMapping("")
    public ResponseEntity<?> addCategory(@RequestBody ChangeCategoryReq req) {
        contentCategoryService.addCategory(req);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteCategory(@RequestBody ChangeCategoryReq req) {
        contentCategoryService.deleteCategory(req);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }
}
