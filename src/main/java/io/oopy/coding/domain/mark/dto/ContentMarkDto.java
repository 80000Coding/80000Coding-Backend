package io.oopy.coding.domain.mark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ContentMarkDto {
    private Long like;
    private Long bookmark;
}
