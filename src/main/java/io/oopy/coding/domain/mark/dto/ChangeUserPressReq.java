package io.oopy.coding.domain.mark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChangeUserPressReq {

    @NotNull
    private Long contentId;

    @NotNull
    private String type;
}
