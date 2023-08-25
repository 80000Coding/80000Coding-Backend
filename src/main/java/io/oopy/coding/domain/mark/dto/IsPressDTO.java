package io.oopy.coding.domain.mark.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IsPressDTO {
    private long like;
    private long bookmark;
}