package com.sanghee.architecture_study.infra.post.entity;

import com.sanghee.architecture_study.infra.comment.entity.CommentJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "post")
public class PostJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<CommentJpaEntity> comments = List.of();

    public PostJpaEntity() {}

    public PostJpaEntity(Integer id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
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

    public List<CommentJpaEntity> getComments() {
        return comments;
    }
}
