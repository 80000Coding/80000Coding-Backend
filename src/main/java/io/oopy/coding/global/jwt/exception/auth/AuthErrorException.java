package io.oopy.coding.global.jwt.exception.auth;

import io.jsonwebtoken.JwtException;

public class AuthErrorException extends JwtException {
    private final AuthErrorCode errorCode;
    private final String causedBy;

    public AuthErrorException(AuthErrorCode errorCode, String causedBy) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.causedBy = causedBy;
    }

    @Override public String toString() {
        return "AuthErrorException{" +
                "errorCode=" + errorCode +
                ", causedBy='" + causedBy + '\'' +
                '}';
    }
}
