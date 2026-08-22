package com.sanghee.architecture_study.presentation;

import com.sanghee.architecture_study.application.PostService;
import com.sanghee.architecture_study.application.command.PostCreateCommand;
import com.sanghee.architecture_study.application.command.PostDeleteCommand;
import com.sanghee.architecture_study.application.command.PostUpdateCommand;
import com.sanghee.architecture_study.application.dto.PostDto;
import com.sanghee.architecture_study.application.dto.PostSummaryDto;
import com.sanghee.architecture_study.application.query.PostGetQuery;
import com.sanghee.architecture_study.presentation.request.PostCreateRequest;
import com.sanghee.architecture_study.presentation.request.PostUpdateRequest;
import com.sanghee.architecture_study.presentation.response.PostCreateResponse;
import com.sanghee.architecture_study.presentation.response.PostGetResponse;
import com.sanghee.architecture_study.presentation.response.PostSummaryResponse;
import com.sanghee.architecture_study.presentation.response.PostUpdateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostSummaryResponse>> getPosts() {
        List<PostSummaryDto> summaries = postService.getPostSummaries();
        List<PostSummaryResponse> response = summaries.stream()
                .map(dto -> new PostSummaryResponse(dto.id(), dto.title(), dto.commentCount()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostGetResponse> getPost(@PathVariable int id) {
        PostDto postDto = postService.getPost(new PostGetQuery(id));
        return ResponseEntity.ok(
                new PostGetResponse(
                        postDto.id(),
                        postDto.title(),
                        postDto.content()
                )
        );
    }

    @PostMapping("/posts")
    public ResponseEntity<PostCreateResponse> createPost(@RequestBody PostCreateRequest postCreateRequest) {
        PostDto postDto = postService.createPost(
                new PostCreateCommand(
                        postCreateRequest.title(),
                        postCreateRequest.content()
                )
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(postDto.id())
                .toUri();

        return ResponseEntity.created(location).body(
                new PostCreateResponse(
                        postDto.id(),
                        postDto.title(),
                        postDto.content()
                )
        );
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostUpdateResponse> updatePost(@PathVariable int id, @RequestBody PostUpdateRequest postUpdateRequest) {
        PostDto postDto = postService.updatePost(
                new PostUpdateCommand(
                        id,
                        postUpdateRequest.title(),
                        postUpdateRequest.content()
                )
        );
        return ResponseEntity.ok(
                new PostUpdateResponse(
                        postDto.id(),
                        postDto.title(),
                        postDto.content()
                )
        );
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable int id) {
        postService.deletePost(new PostDeleteCommand(id));
        return ResponseEntity.noContent().build();
    }
}
