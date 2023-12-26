package io.oopy.coding.api.mark.exception;

import io.oopy.coding.common.response.code.StatusCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ContentMarkErrorCode implements StatusCode {
    /**
     * 400 BAD_REQUEST
     */
    INVALID_TYPE(BAD_REQUEST, "잘못된 타입입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
