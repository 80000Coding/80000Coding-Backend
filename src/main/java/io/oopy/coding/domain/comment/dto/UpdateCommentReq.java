package io.oopy.coding.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@Schema(description = "댓글 수정 DTO")
public class UpdateCommentReq {

    @Schema(description = "댓글 작성자 ID")
    private Long user_id;
    @Schema(description = "댓글 ID")
    private Long comment_id;
    @Schema(description = "댓글 내용")
    private String content;
}
