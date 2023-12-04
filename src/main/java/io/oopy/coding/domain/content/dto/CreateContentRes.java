package io.oopy.coding.domain.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Schema(description = "게시글 생성 DTO")
public class CreateContentRes {
    private Long contentId;

    public static CreateContentRes of(Long contentId) {
        return new CreateContentRes(contentId);
    }
}
