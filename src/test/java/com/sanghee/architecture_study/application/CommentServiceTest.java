package com.sanghee.architecture_study.application;

import com.sanghee.architecture_study.application.command.CommentCreateCommand;
import com.sanghee.architecture_study.application.command.CommentDeleteCommand;
import com.sanghee.architecture_study.application.command.CommentUpdateCommand;
import com.sanghee.architecture_study.application.dto.CommentDto;
import com.sanghee.architecture_study.application.query.CommentListQuery;
import com.sanghee.architecture_study.domain.comment.Comment;
import com.sanghee.architecture_study.domain.comment.CommentRepository;
import com.sanghee.architecture_study.domain.post.Post;
import com.sanghee.architecture_study.domain.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

// PostServiceTest와 같은 패턴이다. 다만 CommentService는 CommentRepository뿐 아니라
// PostRepository도 협력 객체로 쓰기 때문에(댓글 관련 작업 전 게시글 존재를 확인하려고),
// 둘 다 Mock으로 만들어서 CommentService 하나만 격리한다.
@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService 단위 테스트")
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("댓글 등록 - 게시글이 존재하면 댓글을 등록한다")
    void createComment_success() {
        given(postRepository.getPostById(1))
                .willReturn(Optional.of(new Post(1, "제목", "내용")));
        given(commentRepository.createComment(any(Comment.class)))
                .willReturn(new Comment(10, 1, "댓글 내용"));

        CommentDto result = commentService.createComment(new CommentCreateCommand(1, "댓글 내용"));

        assertThat(result.id()).isEqualTo(10);
        assertThat(result.postId()).isEqualTo(1);
        assertThat(result.content()).isEqualTo("댓글 내용");
    }

    @Test
    @DisplayName("댓글 등록 - 게시글이 없으면 예외가 발생하고 실제 저장은 호출되지 않는다")
    void createComment_throws_whenPostNotFound() {
        given(postRepository.getPostById(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.createComment(new CommentCreateCommand(1, "댓글 내용")))
                .isInstanceOf(RuntimeException.class);

        verify(commentRepository, never()).createComment(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 목록 - 게시글이 존재하면 해당 게시글의 댓글 목록을 반환한다")
    void getComments_returnsListOfDtos() {
        given(postRepository.getPostById(1))
                .willReturn(Optional.of(new Post(1, "제목", "내용")));
        given(commentRepository.getCommentsByPostId(1))
                .willReturn(List.of(
                        new Comment(10, 1, "댓글1"),
                        new Comment(11, 1, "댓글2")
                ));

        List<CommentDto> result = commentService.getComments(new CommentListQuery(1));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).content()).isEqualTo("댓글1");
        assertThat(result.get(1).content()).isEqualTo("댓글2");
    }

    @Test
    @DisplayName("댓글 목록 - 게시글이 없으면 예외가 발생한다")
    void getComments_throws_whenPostNotFound() {
        given(postRepository.getPostById(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.getComments(new CommentListQuery(1)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("댓글 수정 - 자기 게시글의 댓글이면 수정한다")
    void updateComment_updatesExistingComment() {
        given(commentRepository.getCommentById(10))
                .willReturn(Optional.of(new Comment(10, 1, "기존 내용")));
        given(commentRepository.updateComment(any(Comment.class)))
                .willReturn(new Comment(10, 1, "새 내용"));

        CommentDto result = commentService.updateComment(new CommentUpdateCommand(1, 10, "새 내용"));

        assertThat(result.content()).isEqualTo("새 내용");
    }

    @Test
    @DisplayName("댓글 수정 - 댓글이 없으면 예외가 발생한다")
    void updateComment_throws_whenCommentNotFound() {
        given(commentRepository.getCommentById(10)).willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.updateComment(new CommentUpdateCommand(1, 10, "새 내용")))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("댓글 수정 - 댓글이 다른 게시글 소속이면 예외가 발생하고 실제 수정은 호출되지 않는다")
    void updateComment_throws_whenCommentDoesNotBelongToPost() {
        given(commentRepository.getCommentById(10))
                .willReturn(Optional.of(new Comment(10, 99, "내용")));

        assertThatThrownBy(() -> commentService.updateComment(new CommentUpdateCommand(1, 10, "새 내용")))
                .isInstanceOf(RuntimeException.class);

        verify(commentRepository, never()).updateComment(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 삭제 - 자기 게시글의 댓글이면 삭제한다")
    void deleteComment_deletesExistingComment() {
        given(commentRepository.getCommentById(10))
                .willReturn(Optional.of(new Comment(10, 1, "내용")));

        commentService.deleteComment(new CommentDeleteCommand(1, 10));

        verify(commentRepository).deleteComment(10);
    }

    @Test
    @DisplayName("댓글 삭제 - 댓글이 없으면 예외가 발생하고 실제 삭제는 호출되지 않는다")
    void deleteComment_throws_whenCommentNotFound() {
        given(commentRepository.getCommentById(10)).willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.deleteComment(new CommentDeleteCommand(1, 10)))
                .isInstanceOf(RuntimeException.class);

        verify(commentRepository, never()).deleteComment(anyInt());
    }

    @Test
    @DisplayName("댓글 삭제 - 댓글이 다른 게시글 소속이면 예외가 발생하고 실제 삭제는 호출되지 않는다")
    void deleteComment_throws_whenCommentDoesNotBelongToPost() {
        given(commentRepository.getCommentById(10))
                .willReturn(Optional.of(new Comment(10, 99, "내용")));

        assertThatThrownBy(() -> commentService.deleteComment(new CommentDeleteCommand(1, 10)))
                .isInstanceOf(RuntimeException.class);

        verify(commentRepository, never()).deleteComment(anyInt());
    }
}
