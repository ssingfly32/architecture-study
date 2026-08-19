package com.sanghee.architecture_study.domain.post;

import com.sanghee.architecture_study.common.exception.BusinessException;
import com.sanghee.architecture_study.common.exception.ErrorCode;

// POJO: plain of java object
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
            throw new BusinessException(ErrorCode.INVALID_TITLE);
        }
    }

    private static void validateContent(String content) {
        if (content.length() > 1000) {
            throw new BusinessException(ErrorCode.INVALID_CONTENT);
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
