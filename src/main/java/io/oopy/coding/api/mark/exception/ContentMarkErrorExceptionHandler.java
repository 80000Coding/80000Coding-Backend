package io.oopy.coding.api.mark.exception;

import io.oopy.coding.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ContentMarkErrorExceptionHandler {

    @ExceptionHandler(ContentMarkErrorException.class)
    protected ResponseEntity<ErrorResponse> handleContentMarkErrorException(ContentMarkErrorException e) {
        log.warn("handleContentMarkErrorException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }
}
