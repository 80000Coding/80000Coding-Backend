package io.oopy.coding.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDto {
    private String access;
    private String refresh;

    public static TokenDto of(String access, String refresh) {
        return new TokenDto(access, refresh);
    }
    public static TokenDto empty() {
        return new TokenDto("", "");
    }
}
