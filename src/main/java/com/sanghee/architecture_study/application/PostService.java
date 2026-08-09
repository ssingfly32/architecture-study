package com.sanghee.architecture_study.application;

import com.sanghee.architecture_study.application.command.PostCreateCommand;
import com.sanghee.architecture_study.application.dto.PostDto;
import com.sanghee.architecture_study.application.query.PostGetQuery;
import com.sanghee.architecture_study.domain.Post;
import com.sanghee.architecture_study.domain.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostDto getPost(PostGetQuery postGetQuery) {
        Post post = postRepository.getPostById(postGetQuery.id());
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent()
        );
    }

    public PostDto createPost(PostCreateCommand postCreateCommand) {
        Post post = postRepository.createPost(
                new Post(
                        null,
                        postCreateCommand.title(),
                        postCreateCommand.content()
                )
        );
        return new PostDto(post.getId(), post.getTitle(), post.getContent());
    }
}
