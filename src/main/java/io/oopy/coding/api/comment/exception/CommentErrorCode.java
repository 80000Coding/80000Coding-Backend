package io.oopy.coding.api.comment.exception;

import io.oopy.coding.common.response.code.StatusCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum CommentErrorCode implements StatusCode {
    /**
     * 400 BAD_REQUEST
     */
    INVALID_COMMENT_ID(BAD_REQUEST, "존재하지 않는 댓글입니다."),
    DELETED_COMMENT(BAD_REQUEST, "삭제된 댓글입니다."),
    INVALID_PARENT_COMMENT(BAD_REQUEST, "부모 댓글이 존재하지 않습니다."),

    /**
     * 401 UNAUTHORIZED
     */
    REQUEST_USER_DATA_OWNER_MISMATCH(UNAUTHORIZED, "댓글 수정 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
