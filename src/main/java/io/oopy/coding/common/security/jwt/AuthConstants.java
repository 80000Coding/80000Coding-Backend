package io.oopy.coding.common.security.jwt;

import lombok.Getter;

@Getter
public enum AuthConstants {
    AUTH_HEADER("Authorization"), TOKEN_TYPE("Bearer "),
    ACCESS_TOKEN("accessToken"), REFRESH_TOKEN("refreshToken"),
    REISSUED_ACCESS_TOKEN("X-Access-Token");

    private final String value;

    AuthConstants(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return String.format("AuthConstants(value=%s)", this.value);
    }
}
