package io.oopy.coding.domain.content.dto;

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
@Schema(description = "게시글 생성 요청 DTO")
public class CreateContentReq {

    @NotBlank
    @Schema(description = "게시글 타입(post, project)", example = "project")
    private String type;

    @Schema(description = "리포지토리 이름", example = "palco repo")
    private String repoName;

    @Schema(description = "리포지토리 오너", example = "palco")
    private String repoOwner;
}
