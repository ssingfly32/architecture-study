package com.sanghee.architecture_study.infra.comment.repository;

import com.sanghee.architecture_study.infra.comment.entity.CommentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentJpaEntity, Integer> {
    List<CommentJpaEntity> findByPostId(Integer postId);
}
