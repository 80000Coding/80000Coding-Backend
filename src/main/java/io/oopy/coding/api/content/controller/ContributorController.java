package io.oopy.coding.api.content.controller;

import io.oopy.coding.api.content.service.ContributorService;
import io.oopy.coding.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "컨트리뷰터", description = "프로젝트에 컨트리뷰터 등록 및 해제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contents/{content_id}/contributor")
public class ContributorController {
    private final ContributorService contributorService;

    @Operation(summary = "컨트리뷰터 조회", description = "프로젝트 1개의 모든 컨트리뷰터를 조회하는 API")
    @GetMapping("")
    public ResponseEntity<?> getContributors(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId) {
        Object response = contributorService.getAllContributors(contentId);
        if (response == null) {
            return ResponseEntity.ok().body(SuccessResponse.noContent());
        } else {
            return ResponseEntity.ok().body(SuccessResponse.from(response));
        }
    }

    @Operation(summary = "컨트리뷰터 등록", description = "프로젝트에 컨트리뷰터 등록")
    @PostMapping("/{contributor_id}")
    public ResponseEntity<?> addContributor(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId,
                                            @Parameter(name = "contributor_id", description = "컨트리뷰터 아이디") @PathVariable(name = "contributor_id") Long contributorId) {
        contributorService.addContributor(contentId, contributorId);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }

    @Operation(summary = "컨트리뷰터 해제", description = "프로젝트에 등록된 컨트리뷰터 해제")
    @DeleteMapping("/{contributor_id}")
    public ResponseEntity<?> deleteContributor(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId,
                                               @Parameter(name = "contributor_id", description = "컨트리뷰터 아이디") @PathVariable(name = "contributor_id") Long contributorId) {
        contributorService.deleteContributor(contentId, contributorId);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }


}
