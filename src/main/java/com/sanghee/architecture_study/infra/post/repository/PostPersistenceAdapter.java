package com.sanghee.architecture_study.infra.post.repository;

import com.sanghee.architecture_study.domain.Post;
import com.sanghee.architecture_study.domain.PostRepository;
import com.sanghee.architecture_study.infra.post.entity.PostJpaEntity;
import org.springframework.stereotype.Repository;

@Repository
public class PostPersistenceAdapter implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    public PostPersistenceAdapter(PostJpaRepository postJpaRepository) {
        this.postJpaRepository = postJpaRepository;
    }

    @Override
    public Post getPostById(int id) {
        PostJpaEntity postJpaEntity = postJpaRepository.getById(id);
        return new Post(postJpaEntity.getId(), postJpaEntity.getTitle(), postJpaEntity.getContent());
    }

    @Override
    public Post createPost(Post post) {
        PostJpaEntity postJpaEntity = new PostJpaEntity(post.getId(), post.getTitle(), post.getContent());
        PostJpaEntity savedPost = postJpaRepository.save(postJpaEntity);
        return new Post(savedPost.getId(), savedPost.getTitle(), savedPost.getContent());
    }
}
