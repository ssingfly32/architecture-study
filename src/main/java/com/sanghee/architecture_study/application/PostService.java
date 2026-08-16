package com.sanghee.architecture_study.application;

import com.sanghee.architecture_study.application.command.PostCreateCommand;
import com.sanghee.architecture_study.application.command.PostDeleteCommand;
import com.sanghee.architecture_study.application.command.PostUpdateCommand;
import com.sanghee.architecture_study.application.dto.PostDto;
import com.sanghee.architecture_study.application.dto.PostSummaryDto;
import com.sanghee.architecture_study.application.query.PostGetQuery;
import com.sanghee.architecture_study.domain.post.Post;
import com.sanghee.architecture_study.domain.post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public PostDto getPost(PostGetQuery postGetQuery) {
        Post post = postRepository.getPostById(postGetQuery.id())
                .orElseThrow(() -> new RuntimeException("post not found"));
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent()
        );
    }

    @Transactional
    public PostDto createPost(PostCreateCommand postCreateCommand) {
        Post post = postRepository.createPost(
                Post.create(
                        postCreateCommand.title(),
                        postCreateCommand.content()
                )
        );
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent()
        );
    }

    @Transactional
    public PostDto updatePost(PostUpdateCommand postUpdateCommand) {
        Post post = postRepository.getPostById(postUpdateCommand.id())
                .orElseThrow(() -> new RuntimeException("post not found"));
        Post updated = post.update(postUpdateCommand.title(), postUpdateCommand.content());
        Post saved = postRepository.updatePost(updated);
        return new PostDto(
                saved.getId(),
                saved.getTitle(),
                saved.getContent()
        );
    }

    @Transactional
    public void deletePost(PostDeleteCommand postDeleteCommand) {
        Post post = postRepository.getPostById(postDeleteCommand.id())
                .orElseThrow(() -> new RuntimeException("post not found"));
        postRepository.deletePost(post.getId());
    }

    @Transactional(readOnly = true)
    public List<PostSummaryDto> getPostSummaries() {
        return postRepository.getAllPostSummaries().stream()
                .map(summary -> new PostSummaryDto(
                        summary.getId(),
                        summary.getTitle(),
                        summary.getCommentCount()
                ))
                .toList();
    }
}
