package io.oopy.coding.common.response.handler;

import io.oopy.coding.api.comment.exception.CommentErrorException;
import io.oopy.coding.api.content.exception.ContentErrorException;
import io.oopy.coding.api.mark.exception.ContentMarkErrorException;
import io.oopy.coding.common.response.ErrorResponse;
import io.oopy.coding.common.response.FailureResponse;
import io.oopy.coding.common.response.code.ErrorCode;
import io.oopy.coding.common.response.exception.GlobalErrorException;
import io.oopy.coding.common.security.jwt.exception.AuthErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Controller에서 발생하는 예외를 처리하는 클래스
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * API 호출 시 서버에서 발생시킨 전역 예외를 처리하는 메서드
     * @param e GlobalErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(GlobalErrorException.class)
    protected ResponseEntity<ErrorResponse> handleGlobalErrorException(GlobalErrorException e) {
        log.warn("handleGlobalErrorException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }

    /**
     * API 호출 시 인증 관련 예외를 처리하는 메서드
     * @param e AuthErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(AuthErrorException.class)
    protected ResponseEntity<ErrorResponse> handleAuthErrorException(AuthErrorException e) {
        log.warn("handleAuthErrorException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }

    /**
     * API 호출 시 인가 관련 예외를 처리하는 메서드
     * @param e AccessDeniedException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("handleAccessDeniedException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FORBIDDEN_ERROR.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * API 호출 시 게시글 관련 예외를 처리하는 메서드
     * @param e ContentErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(ContentErrorException.class)
    protected ResponseEntity<ErrorResponse> handleContentErrorException(ContentErrorException e) {
        log.warn("handleContentErrorException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }

    /**
     * API 호출 시 댓글 관련 예외를 처리하는 메서드
     * @param e CommentErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(CommentErrorException.class)
    protected ResponseEntity<ErrorResponse> handleCommentErrorException(CommentErrorException e) {
        log.warn("handleCommentErrorException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }

    /**
     * API 호출 시 마크 관련 예외를 처리하는 메서드
     * @param e ContentMarkErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(ContentMarkErrorException.class)
    protected ResponseEntity<ErrorResponse> handleContentMarkErrorException(ContentMarkErrorException e) {
        log.warn("handleContentMarkErrorException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }




    /**
     * API 호출 시 객체 혹은 파라미터 데이터 값이 유효하지 않은 경우
     * @param e MethodArgumentNotValidException
     * @return ResponseEntity<FailureResponse>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<FailureResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("handleMethodArgumentNotValidException: {}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        final FailureResponse response = FailureResponse.from(bindingResult);
        return ResponseEntity.unprocessableEntity().body(response);
    }

    /**
     * API 호출 시 'Header' 내에 데이터 값이 유효하지 않은 경우
     * @param e MissingRequestHeaderException
     * @return ResponseEntity<FailureResponse>
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<FailureResponse> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        log.warn("handleMissingRequestHeaderException : {}", e.getMessage());
        final FailureResponse response = FailureResponse.of("causedBy", e.getMessage());
        return ResponseEntity.unprocessableEntity().body(response);
    }

    /**
     * API 호출 시 'BODY' 내에 데이터 값이 존재하지 않은 경우
     * @param e HttpMessageNotReadableException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("handleHttpMessageNotReadableException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MISSING_REQUEST_BODY_ERROR.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * API 호출 시 'Parameter' 내에 데이터 값이 존재하지 않은 경우
     * @param e MissingServletRequestParameterException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("handleMissingServletRequestParameterException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MISSING_REQUEST_PARAMETER_ERROR.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 잘못된 URL 호출 시
     * @param e NoHandlerFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("handleNoHandlerFoundException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_FOUND_ERROR.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * NullPointerException이 발생한 경우
     * @param e NullPointerException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        log.warn("handleNullPointerException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NULL_POINT_ERROR.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }

    // ================================================================================== //

    /**
     * 기타 예외가 발생한 경우
     * @param e Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.warn("handleException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }
}