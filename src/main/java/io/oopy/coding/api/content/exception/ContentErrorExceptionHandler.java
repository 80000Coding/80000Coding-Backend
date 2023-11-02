package io.oopy.coding.api.content.exception;

import io.oopy.coding.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ContentErrorExceptionHandler {

    @ExceptionHandler(ContentErrorException.class)
    protected ResponseEntity<ErrorResponse> handleContentErrorException(ContentErrorException e) {
        log.warn("handleContentErrorException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }
}
