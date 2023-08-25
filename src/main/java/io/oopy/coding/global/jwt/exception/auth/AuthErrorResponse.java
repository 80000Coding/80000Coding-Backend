package io.oopy.coding.global.jwt.exception.auth;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AuthErrorResponse {
    private String code;
    private String message;

    public AuthErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}