package com.sanghee.architecture_study.presentation;

import com.sanghee.architecture_study.application.PostService;
import com.sanghee.architecture_study.application.command.PostCreateCommand;
import com.sanghee.architecture_study.application.query.PostGetQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/{id}")
    public void getPost(@PathVariable int id) {
        postService.getPost(new PostGetQuery(id));
    }

    @PostMapping("/posts")
    public void createPost(@RequestBody PostCreateCommand postCreateCommand) {
        postService.createPost(postCreateCommand);
    }
}
