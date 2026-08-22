package com.sanghee.architecture_study.infra.comment.repository;

import com.sanghee.architecture_study.domain.comment.Comment;
import com.sanghee.architecture_study.domain.comment.CommentRepository;
import com.sanghee.architecture_study.infra.config.QueryDslConfig;
import com.sanghee.architecture_study.infra.post.entity.PostJpaEntity;
import com.sanghee.architecture_study.infra.post.repository.PostJpaRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// CommentPersistenceAdapter뿐 아니라 QueryDslConfig도 같이 Import해야 한다 —
// PostJpaRepository가 QueryDSL 커스텀 구현체를 포함하고 있어서, 그 빈을 만드는
// 시점에 JPAQueryFactory가 필요하기 때문이다 (postJpaRepository.getReferenceById만
// 써도 PostJpaRepository 빈 자체는 완전히 구성돼야 한다).
@DataJpaTest
@Import({CommentPersistenceAdapter.class, QueryDslConfig.class})
@Testcontainers
@DisplayName("CommentPersistenceAdapter 통합 테스트")
class CommentPersistenceAdapterTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostJpaRepository postJpaRepository;

    private Integer createPost() {
        PostJpaEntity saved = postJpaRepository.save(new PostJpaEntity(null, "제목", "내용"));
        return saved.getId();
    }

    @Test
    @DisplayName("댓글을 등록하고 게시글 id로 목록을 조회할 수 있다")
    void createAndGetCommentsByPostId() {
        Integer postId = createPost();

        commentRepository.createComment(Comment.create(postId, "댓글 내용"));

        List<Comment> comments = commentRepository.getCommentsByPostId(postId);

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getContent()).isEqualTo("댓글 내용");
        assertThat(comments.get(0).getPostId()).isEqualTo(postId);
    }

    @Test
    @DisplayName("id로 댓글을 단건 조회할 수 있다")
    void getCommentById_returnsComment() {
        Integer postId = createPost();
        Comment created = commentRepository.createComment(Comment.create(postId, "댓글"));

        Optional<Comment> found = commentRepository.getCommentById(created.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getContent()).isEqualTo("댓글");
    }

    @Test
    @DisplayName("존재하지 않는 댓글 id로 조회하면 빈 Optional을 반환한다")
    void getCommentById_returnsEmpty_whenNotFound() {
        assertThat(commentRepository.getCommentById(999999)).isEmpty();
    }

    @Test
    @DisplayName("댓글을 수정하면 반영된다")
    void updateComment_persistsChanges() {
        Integer postId = createPost();
        Comment created = commentRepository.createComment(Comment.create(postId, "원래 내용"));

        commentRepository.updateComment(new Comment(created.getId(), postId, "바뀐 내용"));

        Optional<Comment> reloaded = commentRepository.getCommentById(created.getId());
        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().getContent()).isEqualTo("바뀐 내용");
    }

    @Test
    @DisplayName("댓글을 삭제하면 더 이상 조회되지 않는다")
    void deleteComment_removesComment() {
        Integer postId = createPost();
        Comment created = commentRepository.createComment(Comment.create(postId, "삭제될 댓글"));

        commentRepository.deleteComment(created.getId());

        assertThat(commentRepository.getCommentById(created.getId())).isEmpty();
    }
}
