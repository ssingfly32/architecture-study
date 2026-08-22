package com.sanghee.architecture_study.infra.post.repository;

import com.sanghee.architecture_study.domain.post.PostSummary;
import com.sanghee.architecture_study.infra.comment.entity.CommentJpaEntity;
import com.sanghee.architecture_study.infra.comment.repository.CommentJpaRepository;
import com.sanghee.architecture_study.infra.config.QueryDslConfig;
import com.sanghee.architecture_study.infra.post.entity.PostJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// N+1을 고친 findAllSummaries()가 실제로 LEFT JOIN으로 동작하는지(댓글 0개인
// 게시글도 누락되지 않는지) 실제 Postgres에 대고 검증하는 테스트.
@DataJpaTest
@Import(QueryDslConfig.class)
@Testcontainers
@DisplayName("PostJpaRepositoryCustomImpl(QueryDSL) 통합 테스트")
class PostJpaRepositoryCustomImplTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Autowired
    private CommentJpaRepository commentJpaRepository;

    @Test
    @DisplayName("댓글이 있는 게시글과 없는 게시글 모두 요약 목록에 포함되고, 댓글 수가 정확하다")
    void findAllSummaries_includesPostsWithZeroComments() {
        PostJpaEntity postWithComments = postJpaRepository.save(new PostJpaEntity(null, "댓글 있는 글", "내용1"));
        PostJpaEntity postWithoutComments = postJpaRepository.save(new PostJpaEntity(null, "댓글 없는 글", "내용2"));

        commentJpaRepository.save(new CommentJpaEntity(null, "댓글1", postWithComments));
        commentJpaRepository.save(new CommentJpaEntity(null, "댓글2", postWithComments));

        List<PostSummary> summaries = postJpaRepository.findAllSummaries();

        PostSummary withComments = summaries.stream()
                .filter(s -> s.getId().equals(postWithComments.getId()))
                .findFirst()
                .orElseThrow();
        PostSummary withoutComments = summaries.stream()
                .filter(s -> s.getId().equals(postWithoutComments.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(withComments.getCommentCount()).isEqualTo(2);
        // LEFT JOIN이 아니라 INNER JOIN이었다면 이 게시글은 아예 결과에서 빠졌을 것이다.
        assertThat(withoutComments.getCommentCount()).isEqualTo(0);
    }
}
