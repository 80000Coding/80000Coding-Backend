package io.oopy.coding.domain.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
@Schema(description = "게시글 수정 요청 DTO")
public class UpdateContentReq {
    @Schema(description = "게시글 id", example = "1")
    private Long content_id;

    @Schema(description = "게시글 제목", example = "제목")
    private String title;

    @Schema(description = "게시글 내용", example = "내용")
    private String body;
}
