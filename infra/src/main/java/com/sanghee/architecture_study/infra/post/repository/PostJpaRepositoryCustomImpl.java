package com.sanghee.architecture_study.infra.post.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sanghee.architecture_study.domain.post.PostSummary;
import com.sanghee.architecture_study.infra.comment.entity.QCommentJpaEntity;
import com.sanghee.architecture_study.infra.post.entity.QPostJpaEntity;

import java.util.List;

public class PostJpaRepositoryCustomImpl implements PostJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostJpaRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<PostSummary> findAllSummaries() {
        QPostJpaEntity post = QPostJpaEntity.postJpaEntity;
        QCommentJpaEntity comment = QCommentJpaEntity.commentJpaEntity;

        return queryFactory
                .select(Projections.constructor(
                        PostSummary.class,
                        post.id,
                        post.title,
                        comment.id.count()
                ))
                .from(post)
                .leftJoin(comment).on(comment.post.eq(post))
                .groupBy(post.id, post.title)
                .fetch();
    }
}
