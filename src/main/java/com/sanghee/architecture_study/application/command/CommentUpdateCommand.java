package com.sanghee.architecture_study.application.command;

public record CommentUpdateCommand(int postId, int commentId, String content) {
}
