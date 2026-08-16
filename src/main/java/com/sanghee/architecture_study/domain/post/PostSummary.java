package com.sanghee.architecture_study.domain.post;

public class PostSummary {
    private final Integer id;
    private final String title;
    private final int commentCount;

    public PostSummary(Integer id, String title, int commentCount) {
        this.id = id;
        this.title = title;
        this.commentCount = commentCount;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCommentCount() {
        return commentCount;
    }
}
