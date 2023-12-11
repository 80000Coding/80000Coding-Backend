package io.oopy.coding.domain.mark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChangeUserPressReq {
    private Long contentId;
    private String type;
}
