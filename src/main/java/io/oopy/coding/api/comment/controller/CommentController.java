package io.oopy.coding.api.comment.controller;

import io.oopy.coding.api.comment.service.CommentService;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.comment.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    // TODO 비회원/회원 이용은 Get만 진행, 뒤쪽 url을 구분해서 ignoring에 작성해야할 것 같음
    @Operation(summary = "게시글에 관련된 댓글 가져오기")
    @GetMapping("/get")
    public ResponseEntity<?> getComment(@RequestParam Long contentId) {
        return ResponseEntity.ok().body(SuccessResponse.from(commentService.getComments(contentId)));
    }

    @Operation(summary = "댓글 작성")
    @PostMapping("/post")
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentReq request, @AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(commentService.createComment(request, securityUser)));
    }

    @Operation(summary = "댓글 수정")
    @PatchMapping("/update")
    public ResponseEntity<?> updateComment(@Valid @RequestBody UpdateCommentReq request, @AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(commentService.updateComment(request, securityUser)));
    }

    @Operation(summary = "댓글 삭제(Soft Delete)")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteComment(@RequestParam Long commentId, @AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(commentService.deleteComment(commentId, securityUser)));
    }

}
