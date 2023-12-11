package io.oopy.coding.domain.content.dto;

import io.oopy.coding.domain.content.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ContentCategoryDto {
    private String type;
    private String color;

    public static ContentCategoryDto toDTO(Category category) {
        return ContentCategoryDto.builder()
                .type(category.getName())
                .color(category.getColor())
                .build();
    }
}
