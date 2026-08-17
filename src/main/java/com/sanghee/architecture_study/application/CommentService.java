package com.sanghee.architecture_study.application;

import com.sanghee.architecture_study.application.command.CommentCreateCommand;
import com.sanghee.architecture_study.application.command.CommentDeleteCommand;
import com.sanghee.architecture_study.application.command.CommentUpdateCommand;
import com.sanghee.architecture_study.application.dto.CommentDto;
import com.sanghee.architecture_study.application.query.CommentListQuery;
import com.sanghee.architecture_study.domain.comment.Comment;
import com.sanghee.architecture_study.domain.comment.CommentRepository;
import com.sanghee.architecture_study.domain.post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public CommentDto createComment(CommentCreateCommand commentCreateCommand) {
        postRepository.getPostById(commentCreateCommand.postId())
                .orElseThrow(() -> new RuntimeException("post not found"));

        Comment comment = commentRepository.createComment(
                Comment.create(commentCreateCommand.postId(), commentCreateCommand.content())
        );
        return new CommentDto(
                comment.getId(),
                comment.getPostId(),
                comment.getContent()
        );
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getComments(CommentListQuery commentListQuery) {
        postRepository.getPostById(commentListQuery.postId())
                .orElseThrow(() -> new RuntimeException("post not found"));

        return commentRepository.getCommentsByPostId(commentListQuery.postId()).stream()
                .map(comment -> new CommentDto(
                        comment.getId(),
                        comment.getPostId(),
                        comment.getContent()
                ))
                .toList();
    }

    @Transactional
    public CommentDto updateComment(CommentUpdateCommand commentUpdateCommand) {
        Comment comment = getCommentBelongingToPost(commentUpdateCommand.postId(), commentUpdateCommand.commentId());

        Comment updated = comment.update(commentUpdateCommand.content());
        Comment saved = commentRepository.updateComment(updated);
        return new CommentDto(
                saved.getId(),
                saved.getPostId(),
                saved.getContent()
        );
    }

    @Transactional
    public void deleteComment(CommentDeleteCommand commentDeleteCommand) {
        Comment comment = getCommentBelongingToPost(
                commentDeleteCommand.postId(),
                commentDeleteCommand.commentId()
        );
        commentRepository.deleteComment(comment.getId());
    }

    private Comment getCommentBelongingToPost(int postId, int commentId) {
        Comment comment = commentRepository.getCommentById(commentId)
                .orElseThrow(() -> new RuntimeException("comment not found"));

        if (!comment.getPostId().equals(postId)) {
            throw new RuntimeException("comment does not belong to the post");
        }

        return comment;
    }
}
