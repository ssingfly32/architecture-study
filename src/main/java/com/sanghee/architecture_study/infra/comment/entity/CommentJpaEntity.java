package com.sanghee.architecture_study.infra.comment.entity;

import com.sanghee.architecture_study.infra.post.entity.PostJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comment")
public class CommentJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostJpaEntity post;

    public CommentJpaEntity() {}

    public CommentJpaEntity(Integer id, String content, PostJpaEntity post) {
        this.id = id;
        this.content = content;
        this.post = post;
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public PostJpaEntity getPost() {
        return post;
    }
}
