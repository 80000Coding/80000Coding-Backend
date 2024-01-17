package io.oopy.coding.domain.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Schema(description = "게시글 수정 요청 DTO")
public class UpdateContentReq {

    @NotBlank
    @Schema(description = "게시글 제목", example = "제목")
    private String title;

    @NotNull
    @Schema(description = "게시글 내용", example = "내용")
    private String body;

    @Schema(description = "게시글 사진 주소", example = "picture Url here")
    private String contentImageUrl;

    @NotNull
    @Schema(description = "작성 완료 여부", example = "false")
    private Boolean publish;
}
