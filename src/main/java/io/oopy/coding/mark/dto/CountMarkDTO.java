package io.oopy.coding.mark.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CountMarkDTO {
    private long like;
    private long bookmark;
}
