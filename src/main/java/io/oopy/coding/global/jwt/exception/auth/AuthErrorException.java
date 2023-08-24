package io.oopy.coding.global.jwt.exception.auth;

import io.jsonwebtoken.JwtException;

public class AuthErrorException extends JwtException {
    private final AuthErrorCode errorCode;
    private final String causedBy;

    public AuthErrorException(AuthErrorCode errorCode, String causedBy) {
        super(String.format("AuthErrorException(code=%s, message=%s, causedBy=%s)",
                errorCode.name(), errorCode.getMessage(), causedBy));
        this.errorCode = errorCode;
        this.causedBy = causedBy;
    }
}