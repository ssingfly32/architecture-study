package com.sanghee.architecture_study.domain.comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment createComment(Comment comment);
    List<Comment> getCommentsByPostId(int postId);
    Optional<Comment> getCommentById(int id);
    Comment updateComment(Comment comment);
    void deleteComment(int id);
}
