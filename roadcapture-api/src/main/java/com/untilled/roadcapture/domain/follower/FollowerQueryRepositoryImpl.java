package com.untilled.roadcapture.domain.follower;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.untilled.roadcapture.api.dto.follower.FollowersCondition;
import com.untilled.roadcapture.api.dto.follower.FollowingsCondition;
import com.untilled.roadcapture.api.dto.follower.FollowingsSortByAlbumResponse;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.domain.album.QAlbum;
import com.untilled.roadcapture.domain.user.QUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.untilled.roadcapture.domain.album.QAlbum.*;
import static com.untilled.roadcapture.domain.follower.QFollower.*;
import static com.untilled.roadcapture.domain.user.QUser.*;

@Slf4j
@Repository
public class FollowerQueryRepositoryImpl extends QuerydslRepositorySupport implements FollowerQueryRepository {

    private final JPAQueryFactory queryFactory;

    public FollowerQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Follower.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<UsersResponse> getFollowings(FollowingsCondition cond, Pageable pageable, Long fromUserId) {

        JPAQuery<UsersResponse> query = queryFactory
                .select(Projections.constructor(UsersResponse.class,
                        follower.to.id,
                        follower.to.username,
                        follower.to.profileImageUrl))
                .from(follower)
                .join(follower.to)
                .where(follower.from.id.eq(fromUserId),
                        toUsernameContains(cond.getUsername()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(follower.to.getType(), follower.to.getMetadata());
            query.orderBy(new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<UsersResponse> result = query.fetchResults();

        return new PageImpl(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<UsersResponse> getFollowers(FollowersCondition cond, Pageable pageable, Long toUserId) {

        JPAQuery<UsersResponse> query = queryFactory
                .select(Projections.constructor(UsersResponse.class,
                        follower.from.id,
                        follower.from.username,
                        follower.from.profileImageUrl))
                .from(follower)
                .join(follower.from)
                .where(follower.to.id.eq(toUserId),
                        fromUsernameContains(cond.getUsername()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(follower.from.getType(), follower.from.getMetadata());
            query.orderBy(new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<UsersResponse> result = query.fetchResults();

        return new PageImpl(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<FollowingsSortByAlbumResponse> getFollowingsSortByAlbum(Pageable pageable, Long fromUserId) {

        List<LocalDateTime> fetch = queryFactory
                .select(album.createdAt.max()).distinct()
                .from(follower)
                .join(follower.to).on(follower.from.id.eq(fromUserId))
                .leftJoin(follower.to.albums, album)
                .groupBy(follower.to.id)
                .fetch();

        JPAQuery<FollowingsSortByAlbumResponse> query = queryFactory
                .select(Projections.constructor(FollowingsSortByAlbumResponse.class,
                        follower.to.id,
                        follower.to.username,
                        follower.to.profileImageUrl,
                        album.createdAt,
                        album.lastModifiedAt
                ))
                .from(follower)
                .join(follower.to).on(follower.from.id.eq(fromUserId))
                .leftJoin(follower.to.albums, album)
                .where(album.createdAt.in(fetch))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(album.getType(), album.getMetadata());
            query.orderBy(new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<FollowingsSortByAlbumResponse> result = query.fetchResults();

        return new PageImpl(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Optional<Follower> getFollowerByFromIdAndToId(Long fromUserId, Long toUserId) {

        QUser fromUser = new QUser("fromUser");
        QUser toUser = new QUser("toUser");

        return Optional.ofNullable(queryFactory
                .selectFrom(QFollower.follower)
                .join(QFollower.follower.from, fromUser).on(fromUser.id.eq(fromUserId))
                .join(QFollower.follower.to, toUser).on(toUser.id.eq(toUserId))
                .fetchFirst());
    }

    private BooleanExpression toUsernameContains(String username) {
        return username != null ? follower.to.username.contains(username) : null;
    }

    private BooleanExpression fromUsernameContains(String username) {
        return username != null ? follower.to.username.contains(username) : null;
    }
}
