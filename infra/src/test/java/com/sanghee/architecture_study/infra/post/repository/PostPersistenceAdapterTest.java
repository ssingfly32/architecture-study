package com.sanghee.architecture_study.infra.post.repository;

import com.sanghee.architecture_study.domain.post.Post;
import com.sanghee.architecture_study.domain.post.PostRepository;
import com.sanghee.architecture_study.infra.config.QueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// PostPersistenceAdapter는 @DataJpaTest의 기본 컴포넌트 스캔 대상(Entity, Spring Data
// 리포지토리 인터페이스)이 아니라서 명시적으로 @Import 해줘야 빈으로 등록된다.
// QueryDslConfig도 필요한데, PostJpaRepository가 QueryDSL 커스텀 구현체(PostJpaRepositoryCustomImpl)를
// 포함하고 있어서 그 빈을 만들 때 JPAQueryFactory가 있어야 하기 때문이다(findAllSummaries를
// 직접 호출하지 않아도, PostJpaRepository 빈 자체를 만드는 시점에 필요하다).
@DataJpaTest
@Import({PostPersistenceAdapter.class, QueryDslConfig.class})
@Testcontainers
@DisplayName("PostPersistenceAdapter 통합 테스트")
class PostPersistenceAdapterTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("게시글을 저장하고 id로 다시 조회할 수 있다")
    void createAndGetPostById() {
        Post created = postRepository.createPost(Post.create("제목", "내용"));

        Optional<Post> found = postRepository.getPostById(created.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("제목");
        assertThat(found.get().getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("존재하지 않는 id로 조회하면 빈 Optional을 반환한다")
    void getPostById_returnsEmpty_whenNotFound() {
        Optional<Post> found = postRepository.getPostById(999999);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("게시글을 수정하면 반영된다")
    void updatePost_persistsChanges() {
        Post created = postRepository.createPost(Post.create("원래 제목", "원래 내용"));

        Post updated = postRepository.updatePost(
                new Post(created.getId(), "바뀐 제목", "바뀐 내용")
        );

        assertThat(updated.getTitle()).isEqualTo("바뀐 제목");
        Optional<Post> reloaded = postRepository.getPostById(created.getId());
        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().getTitle()).isEqualTo("바뀐 제목");
    }

    @Test
    @DisplayName("게시글을 삭제하면 더 이상 조회되지 않는다")
    void deletePost_removesPost() {
        Post created = postRepository.createPost(Post.create("삭제될 글", "내용"));

        postRepository.deletePost(created.getId());

        assertThat(postRepository.getPostById(created.getId())).isEmpty();
    }

    @Test
    @DisplayName("게시글 요약 목록을 조회할 수 있다")
    void getAllPostSummaries_returnsSummaries() {
        postRepository.createPost(Post.create("요약 테스트용 글", "내용"));

        var summaries = postRepository.getAllPostSummaries();

        assertThat(summaries).isNotEmpty();
        assertThat(summaries).anySatisfy(summary -> assertThat(summary.getTitle()).isEqualTo("요약 테스트용 글"));
    }
}
