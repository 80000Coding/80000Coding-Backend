package io.oopy.coding.domain.content.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChangeCategoryReq {
    private Long contentId;
    private String category;
}
