package com.untilled.roadcapture.domain.user;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.untilled.roadcapture.api.dto.user.StudioUserResponse;
import com.untilled.roadcapture.api.dto.user.UsersCondition;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.domain.follower.QFollower;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.untilled.roadcapture.domain.album.QAlbum.album;
import static com.untilled.roadcapture.domain.follower.QFollower.*;
import static com.untilled.roadcapture.domain.user.QUser.user;

@Slf4j
@Repository
public class UserQueryRepositoryImpl extends QuerydslRepositorySupport implements UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public UserQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<UsersResponse> search(Pageable pageable, UsersCondition cond) {

        log.info("offset={}, limit={}", pageable.getOffset(), pageable.getPageSize());
        JPAQuery<UsersResponse> query = queryFactory
                .select(Projections.constructor(UsersResponse.class,
                        user.id,
                        user.username,
                        user.profileImageUrl))
                .from(user)
                .leftJoin(user.albums, album)
                .where(usernameContains(cond.getUsername()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(user.getType(), user.getMetadata());
            query.orderBy(new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<UsersResponse> result = query.fetchResults();

        //카운트 쿼리 필요에 따라 날라감
        return new PageImpl(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Optional<StudioUserResponse> studioUser(Long userId, Long studioUserId) {

        QFollower qFollower = new QFollower("qFollower");
        QFollower qFollowing = new QFollower("qFollowing");

        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(StudioUserResponse.class,
                        user.id,
                        user.username,
                        user.profileImageUrl,
                        user.backgroundImageUrl,
                        user.introduction,
                        ExpressionUtils.as(JPAExpressions.select(qFollower.countDistinct().intValue())
                                        .from(qFollower)
                                        .where(qFollower.to.id.eq(studioUserId))
                                , "followerCount"),
                        ExpressionUtils.as(JPAExpressions.select(qFollowing.countDistinct().intValue())
                                        .from(qFollowing)
                                        .where(qFollowing.from.id.eq(studioUserId))
                                , "followerCount"),
                        ExpressionUtils.as(JPAExpressions.select(follower.isNotNull())
                                        .from(follower)
                                        .where(follower.from.id.eq(userId), follower.to.id.eq(studioUserId))
                                , "followed")
                ))
                .from(user)
                .where(user.id.eq(studioUserId))
                .fetchOne());
    }

    private BooleanExpression usernameContains(String username) {
        return StringUtils.hasText(username) ? user.username.contains(username) : null;
    }
}
