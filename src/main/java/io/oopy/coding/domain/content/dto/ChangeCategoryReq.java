package io.oopy.coding.domain.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChangeCategoryReq {

    @NotNull
    @Schema(description = "게시글 번호", example = "1")
    private Long contentId;

    @NotBlank
    @Schema(description = "카테고리 타입", example = "spring")
    private String category;
}
