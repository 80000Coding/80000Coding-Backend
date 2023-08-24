package io.oopy.coding.comment.controller;

import io.oopy.coding.comment.service.CreateComment;
import io.oopy.coding.comment.service.DeleteComment;
import io.oopy.coding.comment.service.GetComment;
import io.oopy.coding.comment.service.UpdateComment;
import io.oopy.coding.domain.comment.dto.CreateCommentDTO;
import io.oopy.coding.domain.comment.dto.DeleteCommentDTO;
import io.oopy.coding.domain.comment.dto.GetCommentDTO;
import io.oopy.coding.domain.comment.dto.UpdateCommentDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final GetComment getCommentService;
    private final CreateComment createCommentService;
    private final UpdateComment updateCommentService;
    private final DeleteComment deleteCommentService;

    @Operation(summary = "댓글 정보", description = "댓글 정보")
    @GetMapping("")
    @ResponseBody
    public List<GetCommentDTO> getComment(
            @RequestParam Long contentId
    ) {
        return getCommentService.getComments(contentId);
    }

    @Operation(summary = "댓글 생성", description = "Request로 content_id, content, parent_id")
    @PostMapping("")
    @ResponseBody
    public CreateCommentDTO createComment(
            @RequestParam Long userId,
            @RequestParam Long contentId,
            @RequestParam String commentBody,
            @RequestParam(required = false) Long parentId
    ) {
        return createCommentService.createComment(userId, contentId, commentBody, parentId);
    }

    @Operation(summary = "댓글 수정", description = "Request로 content_id, content")
    @PatchMapping("")
    @ResponseBody
    public UpdateCommentDTO updateComment(
            @RequestParam Long commentId,
            @RequestParam String commentBody
    ) {
        return updateCommentService.updateComment(commentId, commentBody);
    }

    @Operation(summary = "댓글 삭제", description = "Request로 content_id")
    @DeleteMapping("")
    @ResponseBody
    public DeleteCommentDTO deleteComment(
            @RequestParam Long commentId
    ) {
        return deleteCommentService.deleteComment(commentId);
    }
}
