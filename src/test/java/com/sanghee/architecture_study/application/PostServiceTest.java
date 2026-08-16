package com.sanghee.architecture_study.application;

import com.sanghee.architecture_study.application.command.PostCreateCommand;
import com.sanghee.architecture_study.application.command.PostDeleteCommand;
import com.sanghee.architecture_study.application.command.PostUpdateCommand;
import com.sanghee.architecture_study.application.dto.PostDto;
import com.sanghee.architecture_study.application.dto.PostSummaryDto;
import com.sanghee.architecture_study.application.query.PostGetQuery;
import com.sanghee.architecture_study.domain.post.Post;
import com.sanghee.architecture_study.domain.post.PostRepository;
import com.sanghee.architecture_study.domain.post.PostSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostService 단위 테스트")
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시글을 등록한다")
    void createPost_success() {
        // given
        PostCreateCommand command = new PostCreateCommand("게시글 등록 테스트", "테스트 시작합니다.");
        given(postRepository.createPost(any(Post.class)))
                .willReturn(new Post(1, "게시글 등록 테스트", "테스트 시작합니다."));

        // when
        PostDto result = postService.createPost(command);

        // then
        assertThat(result.id()).isEqualTo(1);
        assertThat(result.title()).isEqualTo("게시글 등록 테스트");
        assertThat(result.content()).isEqualTo("테스트 시작합니다.");

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).createPost(postCaptor.capture());
        assertThat(postCaptor.getValue().getTitle()).isEqualTo("게시글 등록 테스트");
        assertThat(postCaptor.getValue().getContent()).isEqualTo("테스트 시작합니다.");
    }

    @Test
    @DisplayName("게시글 조회 - 존재하면 DTO를 반환한다")
    void getPost_returnsDto_whenPostExists() {
        given(postRepository.getPostById(1))
                .willReturn(Optional.of(new Post(1, "제목", "내용")));

        PostDto result = postService.getPost(new PostGetQuery(1));

        assertThat(result.id()).isEqualTo(1);
        assertThat(result.title()).isEqualTo("제목");
        assertThat(result.content()).isEqualTo("내용");
    }

    @Test
    @DisplayName("게시글 조회 - 없으면 예외가 발생한다")
    void getPost_throws_whenPostNotFound() {
        given(postRepository.getPostById(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getPost(new PostGetQuery(1)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("게시글 수정 - 존재하는 게시글을 수정한다")
    void updatePost_updatesExistingPost() {
        given(postRepository.getPostById(1))
                .willReturn(Optional.of(new Post(1, "기존 제목", "기존 내용")));
        given(postRepository.updatePost(any(Post.class)))
                .willReturn(new Post(1, "새 제목", "새 내용"));

        PostDto result = postService.updatePost(new PostUpdateCommand(1, "새 제목", "새 내용"));

        assertThat(result.title()).isEqualTo("새 제목");
        assertThat(result.content()).isEqualTo("새 내용");

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).updatePost(postCaptor.capture());
        assertThat(postCaptor.getValue().getId()).isEqualTo(1);
        assertThat(postCaptor.getValue().getTitle()).isEqualTo("새 제목");
    }

    @Test
    @DisplayName("게시글 수정 - 없는 게시글이면 예외가 발생하고 실제 수정은 호출되지 않는다")
    void updatePost_throws_whenPostNotFound() {
        given(postRepository.getPostById(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> postService.updatePost(new PostUpdateCommand(1, "제목", "내용")))
                .isInstanceOf(RuntimeException.class);

        verify(postRepository, never()).updatePost(any(Post.class));
    }

    @Test
    @DisplayName("게시글 삭제 - 존재하는 게시글을 삭제한다")
    void deletePost_deletesExistingPost() {
        given(postRepository.getPostById(1))
                .willReturn(Optional.of(new Post(1, "제목", "내용")));

        postService.deletePost(new PostDeleteCommand(1));

        verify(postRepository).deletePost(1);
    }

    @Test
    @DisplayName("게시글 삭제 - 없는 게시글이면 예외가 발생하고 실제 삭제는 호출되지 않는다")
    void deletePost_throws_whenPostNotFound() {
        given(postRepository.getPostById(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> postService.deletePost(new PostDeleteCommand(1)))
                .isInstanceOf(RuntimeException.class);

        verify(postRepository, never()).deletePost(1);
    }

    @Test
    @DisplayName("게시글 목록 - 댓글 수를 포함한 요약 목록을 반환한다")
    void getPostSummaries_returnsListOfSummaries() {
        given(postRepository.getAllPostSummaries())
                .willReturn(List.of(
                        new PostSummary(1, "제목1", 3),
                        new PostSummary(2, "제목2", 0)
                ));

        List<PostSummaryDto> result = postService.getPostSummaries();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1);
        assertThat(result.get(0).commentCount()).isEqualTo(3);
        assertThat(result.get(1).commentCount()).isEqualTo(0);
    }
}
