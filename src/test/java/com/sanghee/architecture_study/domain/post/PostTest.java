package com.sanghee.architecture_study.domain.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Post는 아무 협력 객체(Repository 등)에도 의존하지 않는 순수한 도메인 객체라서,
// Mock이 하나도 필요 없는 가장 단순한 형태의 단위 테스트다. -> 테스트 코드 작성 용이
@DisplayName("Post 도메인 단위 테스트")
class PostTest {

    @Test
    @DisplayName("제목/내용이 유효하면 게시글을 생성한다")
    void create_success() {
        Post post = Post.create("제목", "내용");

        assertThat(post.getId()).isNull(); // 저장 전이라 id는 아직 없어야 함
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("제목이 50자를 넘으면 예외가 발생한다")
    void create_throws_whenTitleTooLong() {
        String title = "a".repeat(51);

        assertThatThrownBy(() -> Post.create(title, "내용"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("내용이 1000자를 넘으면 예외가 발생한다")
    void create_throws_whenContentTooLong() {
        String content = "a".repeat(1001);

        assertThatThrownBy(() -> Post.create("제목", content))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("update는 기존 id를 유지한 채 title/content만 바뀐 새 인스턴스를 반환한다")
    void update_returnsNewInstanceWithUpdatedFields() {
        Post post = new Post(1, "기존 제목", "기존 내용");

        Post updated = post.update("새 제목", "새 내용");

        assertThat(updated.getId()).isEqualTo(1);
        assertThat(updated.getTitle()).isEqualTo("새 제목");
        assertThat(updated.getContent()).isEqualTo("새 내용");

        // post는 불변 객체이므로 update() 호출 이후에도 원본 값이 그대로여야 한다
        assertThat(post.getTitle()).isEqualTo("기존 제목");
        assertThat(post.getContent()).isEqualTo("기존 내용");
    }

    @Test
    @DisplayName("update도 생성과 동일한 유효성 검증을 통과해야 한다")
    void update_throws_whenTitleTooLong() {
        Post post = new Post(1, "제목", "내용");
        String tooLongTitle = "a".repeat(51);

        assertThatThrownBy(() -> post.update(tooLongTitle, "내용"))
                .isInstanceOf(RuntimeException.class);
    }
}
