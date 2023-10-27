package io.oopy.coding.domain.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Builder
@Schema(description = "게시글 생성 DTO")
public class CreateContentRes {
    private Long content_id;
}
