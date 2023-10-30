package io.oopy.coding.comment.controller;

import io.oopy.coding.comment.service.*;
import io.oopy.coding.domain.comment.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("")
    public ResponseEntity<?> getComment(@RequestParam Long contentId) {
        return ResponseEntity.ok().body(commentService.getComments(contentId));
    }

    @PostMapping("")
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentReq request) {
        return ResponseEntity.ok().body(commentService.createComment(request));
    }

    @Valid
    @PatchMapping("")
    public ResponseEntity<?> updateComment(@Valid @RequestBody UpdateCommentReq request) {
        return ResponseEntity.ok().body(commentService.updateComment(request));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteComment(@RequestParam Long commentId) {
        return ResponseEntity.ok().body(commentService.deleteComment(commentId));
    }

}
