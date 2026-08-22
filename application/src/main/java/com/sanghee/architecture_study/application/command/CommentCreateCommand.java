package com.sanghee.architecture_study.application.command;

public record CommentCreateCommand(int postId, String content) {
}
