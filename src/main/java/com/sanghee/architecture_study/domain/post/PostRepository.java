package com.sanghee.architecture_study.domain.post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> getPostById(int id);
    Post createPost(Post post);
    Post updatePost(Post post);
    void deletePost(int id);
    List<PostSummary> getAllPostSummaries();
}
