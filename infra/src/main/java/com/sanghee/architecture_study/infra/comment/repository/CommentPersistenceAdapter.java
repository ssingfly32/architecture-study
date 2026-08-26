package com.sanghee.architecture_study.infra.comment.repository;

import com.sanghee.architecture_study.domain.comment.Comment;
import com.sanghee.architecture_study.domain.comment.CommentRepository;
import com.sanghee.architecture_study.infra.comment.entity.CommentJpaEntity;
import com.sanghee.architecture_study.infra.post.entity.PostJpaEntity;
import com.sanghee.architecture_study.infra.post.repository.PostJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentPersistenceAdapter implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;
    private final PostJpaRepository postJpaRepository;

    public CommentPersistenceAdapter(CommentJpaRepository commentJpaRepository, PostJpaRepository postJpaRepository) {
        this.commentJpaRepository = commentJpaRepository;
        this.postJpaRepository = postJpaRepository;
    }

    @Override
    public Comment createComment(Comment comment) {
        PostJpaEntity postReference = postJpaRepository.getReferenceById(comment.getPostId());

        CommentJpaEntity saved = commentJpaRepository.save(
                new CommentJpaEntity(
                        null,
                        comment.getContent(),
                        postReference
                )
        );

        return new Comment(
                saved.getId(),
                comment.getPostId(),
                saved.getContent()
        );
    }

    @Override
    public List<Comment> getCommentsByPostId(int postId) {
        return commentJpaRepository.findByPostId(postId).stream()
                .map(entity -> new Comment(entity.getId(), postId, entity.getContent()))
                .toList();
    }

    @Override
    public Optional<Comment> getCommentById(int id) {
        return commentJpaRepository.findById(id)
                .map(entity -> new Comment(entity.getId(), entity.getPost().getId(), entity.getContent()));
    }

    @Override
    public Comment updateComment(Comment comment) {
        PostJpaEntity postReference = postJpaRepository.getReferenceById(comment.getPostId());

        CommentJpaEntity saved = commentJpaRepository.save(
                new CommentJpaEntity(comment.getId(), comment.getContent(), postReference)
        );

        return new Comment(saved.getId(), comment.getPostId(), saved.getContent());
    }

    @Override
    public void deleteComment(int id) {
        commentJpaRepository.deleteById(id);
    }
}
