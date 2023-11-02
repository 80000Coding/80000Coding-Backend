package io.oopy.coding.api.content.exception;

import lombok.Getter;

@Getter
public class ContentErrorException extends RuntimeException {
    private final ContentErrorCode errorCode;

    public ContentErrorException(ContentErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return String.format("ContentErrorException(code=%s, message=%s)",
                errorCode.name(), errorCode.getMessage());
    }
}
