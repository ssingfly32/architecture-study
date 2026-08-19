package com.sanghee.architecture_study.infra.post.repository;

import com.sanghee.architecture_study.domain.post.PostSummary;

import java.util.List;

public interface PostJpaRepositoryCustom {
    List<PostSummary> findAllSummaries();
}
