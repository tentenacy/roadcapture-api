package com.untilled.roadcapture.domain.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.untilled.roadcapture.domain.album.Album;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class CommentQueryRepositoryImpl extends QuerydslRepositorySupport implements CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    public CommentQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Comment.class);
        this.queryFactory = queryFactory;
    }



}
