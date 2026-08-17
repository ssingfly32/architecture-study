package com.sanghee.architecture_study.domain.comment;

public class Comment {
    private final Integer id;
    private final Integer postId;
    private final String content;

    public Comment(Integer id, Integer postId, String content) {
        this.id = id;
        this.postId = postId;
        this.content = content;
    }

    private static void validateContent(String content) {
        if (content.length() > 1000) {
            throw new RuntimeException("내용은 1000자 이하여야 합니다.");
        }
    }

    public static Comment create(Integer postId, String content) {
        validateContent(content);
        return new Comment(null, postId, content);
    }

    public Comment update(String content) {
        validateContent(content);
        return new Comment(this.id, this.postId, content);
    }

    public Integer getId() {
        return id;
    }

    public Integer getPostId() {
        return postId;
    }

    public String getContent() {
        return content;
    }
}
