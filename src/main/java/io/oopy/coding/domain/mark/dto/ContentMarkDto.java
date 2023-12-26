package io.oopy.coding.domain.mark.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class ContentMarkDto {
    private Long like;
    private Long bookmark;
}
