package io.oopy.coding.domain.mark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountMarkDTO {
    private long like;
    private long bookmark;
}
