package io.oopy.coding.api.content.exception;

import io.oopy.coding.common.response.code.StatusCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ContentErrorCode implements StatusCode {
    /**
     * 400 BAD_REQUEST
     */
    DELETED_CONTENT(BAD_REQUEST, "삭제된 게시글입니다."),
    INVALID_CONTENT_ID(BAD_REQUEST, "존재하지 않는 게시글입니다."),
    INVALID_CONTENT_CATEGORY(BAD_REQUEST, "없는 카테고리입니다"),
    INVALID_CONTENT_MARK(BAD_REQUEST, "존재하지 않는 마크타입입니다."),
    EMPTY_CATEGORY(BAD_REQUEST, "게시글에 설정된 카테고리가 없습니다"),
    ALREADY_APPOINTED_CATEGORY(BAD_REQUEST, "이미 지정된 카테고리입니다."),
    ALREADY_DELETED_CATEGORY(BAD_REQUEST, "이미 존재하지 않는 카테고리입니다."),

    /**
     * 401 UNAUTHORIZED
     */
    REQUEST_USER_DATA_OWNER_MISMATCH(UNAUTHORIZED, "게시글 수정 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
