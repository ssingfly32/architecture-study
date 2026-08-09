package com.sanghee.architecture_study.domain;

public class Post {
    private final Integer id;
    private final String title;
    private final String content;

    public Post(Integer id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
