package io.oopy.coding.comment.controller;

import io.oopy.coding.comment.service.*;
import io.oopy.coding.domain.comment.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 정보", description = "Request로 content_id")
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<?> getComment(@RequestParam Long contentId) {
        return ResponseEntity.ok().body(commentService.getComments(contentId));
    }

    @Operation(summary = "댓글 생성", description = "Request로 user_id, content_id, content, parent_id")
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<?> createComment(@RequestBody CreateCommentReq request) {
        return ResponseEntity.ok().body(commentService.createComment(request));
    }

    @Operation(summary = "댓글 수정", description = "Request로 user_id, comment_id, content")
    @PatchMapping("")
    @ResponseBody
    public ResponseEntity<?> updateComment(@RequestBody UpdateCommentReq request) {
        return ResponseEntity.ok().body(commentService.updateComment(request));
    }

    @Operation(summary = "댓글 삭제(Soft Delete)", description = "Request로 comment_id")
    @DeleteMapping("")
    @ResponseBody
    public ResponseEntity<?> deleteComment(@RequestParam Long commentId) {
        return ResponseEntity.ok().body(commentService.deleteComment(commentId));
    }

}
