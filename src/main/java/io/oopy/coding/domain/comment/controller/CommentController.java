package io.oopy.coding.domain.comment.controller;

import io.oopy.coding.domain.comment.dto.GetCommentDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
