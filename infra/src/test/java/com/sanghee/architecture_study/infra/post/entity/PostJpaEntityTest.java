package com.sanghee.architecture_study.infra.post.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// no-arg 생성자는 JPA 스펙상 필수라서 있지만(하이버네이트가 리플렉션으로 엔티티를
// 만들 때 씀), 우리 코드에서 직접 호출하는 일은 없어서 별도로 커버해준다.
@DisplayName("PostJpaEntity 단위 테스트")
class PostJpaEntityTest {

    @Test
    @DisplayName("no-arg 생성자로 만들면 필드가 모두 비어있다")
    void noArgConstructor_createsEmptyEntity() {
        PostJpaEntity entity = new PostJpaEntity();

        assertThat(entity.getId()).isNull();
        assertThat(entity.getTitle()).isNull();
        assertThat(entity.getContent()).isNull();
        assertThat(entity.getComments()).isEmpty();
    }
}
