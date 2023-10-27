package io.oopy.coding.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "댓글 생성 DTO")
public class CreateCommentReq {

    @Schema(description = "댓글 작성자 ID")
    private Long user_id;
    @Schema(description = "게시글 ID")
    private Long content_id;
    @Schema(description = "댓글 내용")
    private String content;
    @Schema(description = "부모 댓글 ID")
    private Long parent_id;
}
