package io.oopy.coding.common.util.jwt.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getHttpStatus();
    String getMessage();
    String getName();
}
