package io.oopy.coding.api.comment.exception;

import lombok.extern.slf4j.Slf4j;
import io.oopy.coding.common.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommentErrorExceptionHandler {

    @ExceptionHandler(CommentErrorException.class)
    protected ResponseEntity<ErrorResponse> handleCommentErrorException(CommentErrorException e) {
        log.warn("handleCommentErrorException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }
}
