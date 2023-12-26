package io.oopy.coding.domain.mark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserPressReq {
    private boolean like;
    private boolean bookmark;
}
