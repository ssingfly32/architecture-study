package com.sanghee.architecture_study.common.exception;

public enum ErrorCode {
    POST_NOT_FOUND("게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다."),
    COMMENT_POST_MISMATCH("해당 게시글의 댓글이 아닙니다."),
    INVALID_TITLE("제목은 50자 이하여야 합니다."),
    INVALID_CONTENT("내용은 1000자 이하여야 합니다."),
    INTERNAL_ERROR("서버 내부 오류가 발생했습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
