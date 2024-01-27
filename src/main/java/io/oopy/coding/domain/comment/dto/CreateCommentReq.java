package io.oopy.coding.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "댓글 생성 DTO")
public class CreateCommentReq {

    @NotBlank
    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "부모 댓글 ID")
    private Long parentId;
}
