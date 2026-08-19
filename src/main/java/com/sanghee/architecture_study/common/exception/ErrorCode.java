package com.sanghee.architecture_study.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    COMMENT_POST_MISMATCH(HttpStatus.NOT_FOUND, "해당 게시글의 댓글이 아닙니다."),
    INVALID_TITLE(HttpStatus.BAD_REQUEST, "제목은 50자 이하여야 합니다."),
    INVALID_CONTENT(HttpStatus.BAD_REQUEST, "내용은 1000자 이하여야 합니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
