package com.sanghee.architecture_study.infra.post.repository;

import com.sanghee.architecture_study.domain.post.Post;
import com.sanghee.architecture_study.domain.post.PostRepository;
import com.sanghee.architecture_study.domain.post.PostSummary;
import com.sanghee.architecture_study.infra.post.entity.PostJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostPersistenceAdapter implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    public PostPersistenceAdapter(PostJpaRepository postJpaRepository) {
        this.postJpaRepository = postJpaRepository;
    }

    @Override
    public Optional<Post> getPostById(int id) {
        return postJpaRepository.findById(id)
                .map(entity -> new Post(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getContent()
                    )
                );
    }

    @Override
    public Post createPost(Post post) {
        PostJpaEntity postJpaEntity = new PostJpaEntity(post.getId(), post.getTitle(), post.getContent());
        PostJpaEntity savedEntity = postJpaRepository.save(postJpaEntity);
        return new Post(savedEntity.getId(), savedEntity.getTitle(), savedEntity.getContent());
    }

    @Override
    public Post updatePost(Post post) {
        PostJpaEntity savedEntity = postJpaRepository.save(
                new PostJpaEntity(
                    post.getId(),
                    post.getTitle(),
                    post.getContent()
                )
        );
        return new Post(
                savedEntity.getId(),
                savedEntity.getTitle(),
                savedEntity.getContent()
        );
    }

    @Override
    public void deletePost(int id) {
        postJpaRepository.deleteById(id);
    }

    @Override
    public List<PostSummary> getAllPostSummaries() {
        return postJpaRepository.findAllSummaries();
    }
}
