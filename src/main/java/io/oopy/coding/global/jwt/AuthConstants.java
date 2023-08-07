package io.oopy.coding.global.jwt;

import lombok.Getter;

@Getter
public enum AuthConstants {
    AUTH_HEADER("Authorization"), TOKEN_TYPE("Bearer ");

    private String value;

    AuthConstants(String value) {
        this.value = value;
    }
}
