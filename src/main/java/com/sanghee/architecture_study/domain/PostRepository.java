package com.sanghee.architecture_study.domain;

public interface PostRepository {
    Post getPostById(int id);
    Post createPost(Post post);
}
