package io.oopy.coding.common.security.jwt.exception;

public record AuthErrorResponse(String code, String message) {
    @Override public String toString() {
        return String.format("AuthErrorResponse(code=%s, message=%s)", code, message);
    }
}