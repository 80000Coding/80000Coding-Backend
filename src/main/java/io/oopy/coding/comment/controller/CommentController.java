package io.oopy.coding.comment.controller;

import io.oopy.coding.domain.comment.dto.GetCommentDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    @Operation(summary = "댓글 정보", description = "댓글 정보")
    @GetMapping("")
    public GetCommentDTO getComment() {
        return GetCommentDTO.builder()
                .commentBody("test comment")
                .commentCreatedAt(LocalDateTime.now())
                .userName("test user")
                .userProfileImageUrl("test url")
                .build();
    }

    @Operation(summary = "댓글 생성", description = "Request로 content_id, content, parent_id")
    @PostMapping("")
    public boolean createComment() {
        return true;
    }

    @Operation(summary = "댓글 수정", description = "Request로 content_id, content")
    @PatchMapping("")
    public boolean updateComment() {
        return true;
    }

    @Operation(summary = "댓글 삭제", description = "Request로 content_id")
    @DeleteMapping("")
    public boolean deleteComment() {
        return true;
    }
}
