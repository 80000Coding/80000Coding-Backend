package io.oopy.coding.comment.controller;

import io.oopy.coding.comment.dto.*;
import io.oopy.coding.comment.service.CreateComment;
import io.oopy.coding.comment.service.DeleteComment;
import io.oopy.coding.comment.service.GetComment;
import io.oopy.coding.comment.service.UpdateComment;
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

    @Operation(summary = "댓글 정보", description = "Request로 content_id")
    @GetMapping("")
    @ResponseBody
    public GetCommentDTO.Res getComment(@RequestParam Long contentId) {

        GetCommentDTO.Req requestDTO = GetCommentDTO.Req.builder()
                .contentId(contentId)
                .build();
        return getCommentService.getComments(requestDTO);
    }

    @Operation(summary = "댓글 생성", description = "Request로 user_id, content_id, content, parent_id")
    @PostMapping("")
    @ResponseBody
    public CreateCommentDTO.Res createComment(@RequestBody CreateCommentDTO.Req requestDTO) {

        return createCommentService.createComment(requestDTO);
    }

    @Operation(summary = "댓글 수정", description = "Request로 user_id, comment_id, content")
    @PatchMapping("")
    @ResponseBody
    public UpdateCommentDTO.Res updateComment(@RequestBody UpdateCommentDTO.Req requestDTO) {
        return updateCommentService.updateComment(requestDTO);
    }

    @Operation(summary = "댓글 삭제(Soft Delete)", description = "Request로 comment_id")
    @DeleteMapping("")
    @ResponseBody
    public DeleteCommentDTO.Res deleteComment(@RequestParam Long commentId) {

        DeleteCommentDTO.Req requestDTO = DeleteCommentDTO.Req.builder()
                .comment_id(commentId)
                .build();

        return deleteCommentService.deleteComment(requestDTO);
    }

}
