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

    // QueryDSL의 count()는 (내부적으로 생성되는 JPQL의 COUNT()와 마찬가지로) Long을 반환한다.
    // PostJpaRepositoryCustomImpl의 Projections.constructor(...)가 리플렉션으로 이 시그니처에
    // 맞는 생성자를 찾아 호출하기 때문에, Long을 받는 생성자를 별도로 둔다.
    public PostSummary(Integer id, String title, Long commentCount) {
        this(id, title, commentCount.intValue());
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
