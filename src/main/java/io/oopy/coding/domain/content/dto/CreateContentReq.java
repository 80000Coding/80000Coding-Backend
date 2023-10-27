package io.oopy.coding.domain.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@Schema(description = "게시글 생성 요청 DTO")
public class CreateContentReq {
    @Schema(description = "유저 id(임시)", example = "1")
    private Long user_id;

    @Schema(description = "게시글 타입", example = "post")
    private String type;

    @Schema(description = "리포지토리 이름", example = "palco repo")
    private String repo_name;

    @Schema(description = "리포지토리 오너", example = "palco")
    private String repo_owner;
}
