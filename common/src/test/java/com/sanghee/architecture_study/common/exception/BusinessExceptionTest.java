package com.sanghee.architecture_study.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BusinessException / ErrorCode 단위 테스트")
class BusinessExceptionTest {

    @Test
    @DisplayName("ErrorCode의 메시지를 그대로 노출한다")
    void errorCode_exposesMessage() {
        assertThat(ErrorCode.POST_NOT_FOUND.getMessage()).isEqualTo("게시글을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("BusinessException은 ErrorCode를 그대로 보관하고, 예외 메시지는 ErrorCode의 메시지와 같다")
    void businessException_wrapsErrorCode() {
        BusinessException exception = new BusinessException(ErrorCode.COMMENT_NOT_FOUND);

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.COMMENT_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.COMMENT_NOT_FOUND.getMessage());
    }
}
