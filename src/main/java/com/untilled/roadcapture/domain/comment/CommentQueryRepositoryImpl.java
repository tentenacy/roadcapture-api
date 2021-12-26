package com.untilled.roadcapture.domain.comment;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.untilled.roadcapture.api.dto.comment.CommentsResponse;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.domain.album.QAlbum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.untilled.roadcapture.domain.album.QAlbum.album;
import static com.untilled.roadcapture.domain.comment.QComment.*;
import static com.untilled.roadcapture.domain.picture.QPicture.*;
import static com.untilled.roadcapture.domain.user.QUser.*;

@Repository
public class CommentQueryRepositoryImpl extends QuerydslRepositorySupport implements CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    public CommentQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Comment.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<CommentsResponse> getPictureComments(Long pictureId, Pageable pageable) {
        JPAQuery<CommentsResponse> query = queryFactory
                .select(Projections.constructor(CommentsResponse.class,
                        picture.id,
                        comment.createdAt,
                        comment.lastModifiedAt,
                        comment.content,
                        Projections.constructor(UsersResponse.class,
                                user.id,
                                user.username,
                                user.profileImageUrl)))
                .from(comment)
                .join(comment.user, user)
                .join(comment.picture, picture)
                .where(picture.id.eq(pictureId))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(comment.getType(), comment.getMetadata());
            query.orderBy(new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<CommentsResponse> result = query.fetchResults();

        //카운트 쿼리 필요에 따라 날라감
        return new PageImpl(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<CommentsResponse> getAlbumComments(Long albumId, Pageable pageable) {

        JPAQuery<CommentsResponse> query = queryFactory
                .select(Projections.constructor(CommentsResponse.class,
                        picture.id,
                        comment.createdAt,
                        comment.lastModifiedAt,
                        comment.content,
                        Projections.constructor(UsersResponse.class,
                                user.id,
                                user.username,
                                user.profileImageUrl)))
                .from(picture)
                .join(picture.album, album).on(album.id.eq(albumId))
                .join(picture.comments, comment)
                .join(comment.user, user)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(comment.getType(), comment.getMetadata());
            query.orderBy(new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<CommentsResponse> result = query.fetchResults();

        //카운트 쿼리 필요에 따라 날라감
        return new PageImpl(result.getResults(), pageable, result.getTotal());
    }
}
