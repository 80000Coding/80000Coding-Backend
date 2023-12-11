package io.oopy.coding.api.mark.exception;

import lombok.Getter;

@Getter
public class ContentMarkErrorException extends RuntimeException {
    private final ContentMarkErrorCode errorCode;

    public ContentMarkErrorException(ContentMarkErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return String.format("ContentErrorException(code=%s, message=%s)",
                errorCode.name(), errorCode.getMessage());
    }
}
