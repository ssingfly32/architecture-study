package com.sanghee.architecture_study.domain.comment;

import com.sanghee.architecture_study.common.exception.BusinessException;
import com.sanghee.architecture_study.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Comment 도메인 단위 테스트")
class CommentTest {

    @Test
    @DisplayName("내용이 유효하면 댓글을 생성한다")
    void create_success() {
        Comment comment = Comment.create(1, "댓글 내용");

        assertThat(comment.getId()).isNull();
        assertThat(comment.getPostId()).isEqualTo(1);
        assertThat(comment.getContent()).isEqualTo("댓글 내용");
    }

    @Test
    @DisplayName("내용이 1000자를 넘으면 예외가 발생한다")
    void create_throws_whenContentTooLong() {
        String content = "a".repeat(1001);

        assertThatThrownBy(() -> Comment.create(1, content))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_CONTENT);
    }

    @Test
    @DisplayName("update는 id/postId를 유지한 채 content만 바뀐 새 인스턴스를 반환한다")
    void update_returnsNewInstanceWithUpdatedContent() {
        Comment comment = new Comment(1, 10, "기존 내용");

        Comment updated = comment.update("새 내용");

        assertThat(updated.getId()).isEqualTo(1);
        assertThat(updated.getPostId()).isEqualTo(10);
        assertThat(updated.getContent()).isEqualTo("새 내용");

        // comment는 불변 객체이므로 update() 호출 이후에도 원본 값이 그대로여야 한다
        assertThat(comment.getContent()).isEqualTo("기존 내용");
    }
}
