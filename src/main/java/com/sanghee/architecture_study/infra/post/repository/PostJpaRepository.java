package com.sanghee.architecture_study.infra.post.repository;

import com.sanghee.architecture_study.infra.post.entity.PostJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostJpaEntity, Integer> {
}
