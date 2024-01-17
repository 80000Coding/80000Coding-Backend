package io.oopy.coding.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "댓글 수정 DTO")
public class UpdateCommentReq {

    @NotBlank
    @Schema(description = "댓글 내용")
    private String content;
}
