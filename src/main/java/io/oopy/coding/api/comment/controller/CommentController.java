package io.oopy.coding.api.comment.controller;

import io.oopy.coding.api.comment.service.CommentService;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.comment.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "댓글", description = "댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contents/{content_id}/comments")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "게시글에 관련된 댓글 가져오기", description = "content_id 에 등록되어 있는 모든 댓글을 가져오는 API")
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getComment(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId) {
        return ResponseEntity.ok().body(SuccessResponse.from(commentService.getComments(contentId)));
    }

    @Operation(summary = "댓글 작성", description = "content_id 게시물에 댓글 작성")
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createComment(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId,
                                           @Valid @RequestBody CreateCommentReq request,
                                           @AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(commentService.createComment(contentId, request, securityUser)));
    }

    @Operation(summary = "댓글 수정", description = "comment_id 댓글 수정")
    @PatchMapping("/{comment_id}")
    @PreAuthorize("isAuthenticated() && @authorManager.isCommentAuthor(#authentication.getPrincipal(), #commentId)")
    public ResponseEntity<?> updateComment(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId,
                                           @Parameter(name = "comment_id", description = "댓글 번호") @PathVariable(name = "comment_id") Long commentId,
                                           @Valid @RequestBody UpdateCommentReq request,
                                           @AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(commentService.updateComment(commentId, request, securityUser)));
    }

    @Operation(summary = "댓글 삭제", description = "comment_id 댓글 삭제. Soft Delete")
    @DeleteMapping("/{comment_id}")
    @PreAuthorize("isAuthenticated() && @authorManager.isCommentAuthor(#authentication.getPrincipal(), #commentId)")
    public ResponseEntity<?> deleteComment(@Parameter(name = "content_id", description = "게시글 번호") @PathVariable(name = "content_id") Long contentId,
                                           @Parameter(name = "comment_id", description = "댓글 번호") @PathVariable(name = "comment_id") Long commentId,
                                           @AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(commentService.deleteComment(commentId, securityUser)));
    }

}
