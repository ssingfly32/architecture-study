package com.sanghee.architecture_study.domain.post;

public class Post {
    private final Integer id;
    private final String title;
    private final String content;

    public Post(Integer id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    private static void validateTitle(String title) {
        if (title.length() > 50) {
            throw new RuntimeException("제목은 50자 이하여야 합니다.");
        }
    }

    private static void validateContent(String content) {
            if (content.length() > 1000) {
                throw new RuntimeException("내용은 1000자 이하여야 합니다.");
            }
        }

    public static Post create(String title, String content) {
        validateTitle(title);
        validateContent(content);
        return new Post(null, title, content);
    }

    public Post update(String title, String content) {
        validateTitle(title);
        validateContent(content);
        return new Post(this.id, title, content);
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
