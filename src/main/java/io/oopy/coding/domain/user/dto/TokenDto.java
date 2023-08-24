package io.oopy.coding.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDto {
    private String access;

    public static TokenDto of(String access) {
        return new TokenDto(access);
    }
    public static TokenDto empty() {
        return new TokenDto("");
    }
}
