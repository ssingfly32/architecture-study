package com.sanghee.architecture_study.presentation;

import com.sanghee.architecture_study.common.exception.BusinessException;
import com.sanghee.architecture_study.common.exception.ErrorCode;
import com.sanghee.architecture_study.presentation.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("business exception: code={}, message={}", errorCode.name(), errorCode.getMessage());
        return ResponseEntity.status(resolveStatus(errorCode))
                .body(new ErrorResponse(errorCode.name()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("unexpected exception", e);
        return ResponseEntity.status(resolveStatus(ErrorCode.INTERNAL_ERROR))
                .body(new ErrorResponse(ErrorCode.INTERNAL_ERROR.name()));
    }

    // ErrorCode는 순수 비즈니스 의미만 담고, HTTP 상태 코드로의 매핑은
    // presentation 계층(여기)의 책임으로 둔다. HTTP는 여러 전달 방식 중 하나일 뿐이라
    // ErrorCode 자체가 특정 프로토콜에 종속되지 않게 하기 위함.
    private HttpStatus resolveStatus(ErrorCode code) {
        return switch (code) {
            case POST_NOT_FOUND, COMMENT_NOT_FOUND, COMMENT_POST_MISMATCH -> HttpStatus.NOT_FOUND;
            case INVALID_TITLE, INVALID_CONTENT -> HttpStatus.BAD_REQUEST;
            case INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
