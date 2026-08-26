package com.sanghee.architecture_study.domain.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// PostSummary는 int를 받는 생성자와 Long을 받는 생성자가 따로 있다.
// QueryDSL의 count()가 Long을 반환하기 때문에, Projections.constructor가 리플렉션으로
// 시그니처를 맞추려면 Long 생성자가 별도로 필요하다 (PostJpaRepositoryCustomImplTest에서
// 실제 QueryDSL 경로는 검증하고, 여기서는 두 생성자가 같은 결과를 만드는지만 확인한다).
@DisplayName("PostSummary 단위 테스트")
class PostSummaryTest {

    @Test
    @DisplayName("int commentCount 생성자로 값을 그대로 보관한다")
    void constructor_withInt() {
        PostSummary summary = new PostSummary(1, "제목", 3);

        assertThat(summary.getId()).isEqualTo(1);
        assertThat(summary.getTitle()).isEqualTo("제목");
        assertThat(summary.getCommentCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("Long commentCount 생성자는 int로 변환해서 동일하게 보관한다")
    void constructor_withLong() {
        PostSummary summary = new PostSummary(1, "제목", 3L);

        assertThat(summary.getCommentCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("댓글 수가 0이어도 정상적으로 보관한다")
    void constructor_withZeroComments() {
        PostSummary summary = new PostSummary(1, "제목", 0L);

        assertThat(summary.getCommentCount()).isZero();
    }
}
