package io.oopy.coding.api.content.exception;

import io.oopy.coding.common.response.code.StatusCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ContentErrorCode implements StatusCode {
    /**
     * 400 BAD_REQUEST
     */
    DELETED_CONTENT(BAD_REQUEST, "삭제된 게시글입니다."),
    INVALID_CONTENT_ID(BAD_REQUEST, "존재하지 않는 게시글입니다."),

    /**
     * 403 FORBIDDEN
     */
    REQUEST_USER_DATA_OWNER_MISMATCH(FORBIDDEN, "게시글 수정 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
