package com.untilled.roadcapture.domain.album;

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
import com.untilled.roadcapture.api.dto.album.*;
import com.untilled.roadcapture.api.dto.picture.PictureResponse;
import com.untilled.roadcapture.api.dto.picture.ThumbnailPictureResponse;
import com.untilled.roadcapture.api.dto.place.PlaceResponse;
import com.untilled.roadcapture.api.dto.user.AlbumUserResponse;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.domain.like.QLike;
import com.untilled.roadcapture.domain.picture.QPicture;
import com.untilled.roadcapture.domain.user.QUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.untilled.roadcapture.domain.album.QAlbum.album;
import static com.untilled.roadcapture.domain.comment.QComment.*;
import static com.untilled.roadcapture.domain.follower.QFollower.*;
import static com.untilled.roadcapture.domain.like.QLike.*;
import static com.untilled.roadcapture.domain.picture.QPicture.*;
import static com.untilled.roadcapture.domain.place.QPlace.*;
import static com.untilled.roadcapture.domain.user.QUser.user;

@Slf4j
@Repository
public class AlbumQueryRepositoryImpl extends QuerydslRepositorySupport implements AlbumQueryRepository {

    private final JPAQueryFactory queryFactory;

    public AlbumQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Album.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<StudioAlbumsResponse> getStudioAlbums(Pageable pageable, Long userId) {
        JPAQuery<StudioAlbumsResponse> query = queryFactory
                .select(Projections.constructor(StudioAlbumsResponse.class,
                        album.id,
                        album.createdAt,
                        album.lastModifiedAt,
                        album.title,
                        Projections.constructor(ThumbnailPictureResponse.class,
                                picture.id,
                                picture.createdAt,
                                picture.lastModifiedAt,
                                picture.imageUrl)
                ))
                .from(album)
                .join(album.user, user).on(user.id.eq(userId))
                .join(album.pictures, picture).on(picture.isThumbnail.eq(true))
                .join(picture.place, place)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(album.getType(), album.getMetadata());
            query.orderBy(new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<StudioAlbumsResponse> result = query.fetchResults();

        //????????? ?????? ????????? ?????? ?????????
        return new PageImpl(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<AlbumsResponse> getAlbums(AlbumsCondition cond, Pageable pageable, Long userId) {
        QAlbum qAlbum = new QAlbum("qAlbum");
        QPicture qThumbnailPicture = new QPicture("qThumbnailPicture");
        QLike qLike = new QLike("qLike");
        JPAQuery<AlbumsResponse> query = queryFactory
                .select(Projections.constructor(AlbumsResponse.class,
                        album.id,
                        album.createdAt,
                        album.lastModifiedAt,
                        album.title,
                        album.description,
                        Projections.constructor(ThumbnailPictureResponse.class,
                                qThumbnailPicture.id,
                                qThumbnailPicture.createdAt,
                                qThumbnailPicture.lastModifiedAt,
                                qThumbnailPicture.imageUrl),
                        Projections.constructor(UsersResponse.class,
                                user.id,
                                user.username,
                                user.profileImageUrl),
                        album.viewCount,
                        ExpressionUtils.as(JPAExpressions.select(like.countDistinct().intValue())
                                .from(qAlbum)
                                .leftJoin(qAlbum.likes, like)
                                .where(qAlbum.id.eq(album.id)), "likeCount"),
                        ExpressionUtils.as(JPAExpressions.select(comment.countDistinct().intValue())
                                .from(qAlbum)
                                .leftJoin(qAlbum.pictures, picture)
                                .leftJoin(picture.comments, comment)
                                .where(qAlbum.id.eq(album.id)), "commentCount"),
                        qLike.user.isNotNull().as("liked")
                ))
                .from(album)
                .join(album.user, user)
                .leftJoin(album.likes, qLike).on(qLike.user.id.eq(userId))
                .leftJoin(album.pictures, qThumbnailPicture).on(qThumbnailPicture.album.id.eq(album.id)).on(qThumbnailPicture.isThumbnail.eq(true))
                .where(
                        dateTimeLoe(cond.getDateTimeTo()),
                        dateTimeGoe(cond.getDateTimeFrom()),
                        titleContains(cond.getTitle())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(album.getType(), album.getMetadata());
            query.orderBy(new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<AlbumsResponse> result = query.fetchResults();

        //????????? ?????? ????????? ?????? ?????????
        return new PageImpl(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<AlbumsResponse> getFollowingAlbums(FollowingAlbumsCondition cond, Pageable pageable, Long userId) {
        QAlbum qAlbum = new QAlbum("qAlbum");
        QPicture qThumbnailPicture = new QPicture("qThumbnailPicture");
        QUser qFollowingUser = new QUser("qFollowingUser");
        QLike qLike = new QLike("qLike");
        JPAQuery<AlbumsResponse> query = queryFactory
                .select(Projections.constructor(AlbumsResponse.class,
                        album.id,
                        album.createdAt,
                        album.lastModifiedAt,
                        album.title,
                        album.description,
                        Projections.constructor(ThumbnailPictureResponse.class,
                                qThumbnailPicture.id,
                                qThumbnailPicture.createdAt,
                                qThumbnailPicture.lastModifiedAt,
                                qThumbnailPicture.imageUrl),
                        Projections.constructor(UsersResponse.class,
                                qFollowingUser.id,
                                qFollowingUser.username,
                                qFollowingUser.profileImageUrl),
                        album.viewCount,
                        ExpressionUtils.as(JPAExpressions.select(like.countDistinct().intValue())
                                .from(qAlbum)
                                .leftJoin(qAlbum.likes, like)
                                .where(qAlbum.id.eq(album.id)), "likeCount"),
                        ExpressionUtils.as(JPAExpressions.select(comment.countDistinct().intValue())
                                .from(qAlbum)
                                .leftJoin(qAlbum.pictures, picture)
                                .leftJoin(picture.comments, comment)
                                .where(qAlbum.id.eq(album.id)), "commentCount"),
                        qLike.user.isNotNull().as("liked")
                ))
                .from(follower)
                .join(follower.from, user).on(user.id.eq(userId))
                .join(follower.to, qFollowingUser)
                .leftJoin(qFollowingUser.albums, album)
                .leftJoin(album.likes, qLike).on(qLike.user.id.eq(userId))
                .leftJoin(album.pictures, qThumbnailPicture).on(qThumbnailPicture.album.id.eq(album.id)).on(qThumbnailPicture.isThumbnail.eq(true))
                .where(followingIdEq(cond.getFollowingId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(album.getType(), album.getMetadata());
            query.orderBy(new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<AlbumsResponse> result = query.fetchResults();

        return new PageImpl(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Optional<AlbumResponse> getAlbum(Long albumId, Long userId) {
        QLike qLike = new QLike("qLike");
        QPicture qPicture = new QPicture("qPicture");
        AlbumResponse albumResponse = queryFactory
                .select(Projections.constructor(AlbumResponse.class,
                        album.id,
                        album.createdAt,
                        album.lastModifiedAt,
                        album.title,
                        album.description,
                        Projections.constructor(AlbumUserResponse.class,
                                user.id,
                                user.username,
                                user.profileImageUrl,
                                follower.from.isNotNull().as("followed")),
                        album.viewCount,
                        like.countDistinct().intValue().as("likeCount"),
                        comment.countDistinct().intValue().as("commentCount"),
                        qLike.user.isNotNull().as("liked")))
                .from(follower)
                .rightJoin(follower.to, user).on(follower.from.id.eq(userId))
                .leftJoin(user.albums, album).on(album.id.eq(albumId))
                .leftJoin(album.likes, like)
                .leftJoin(album.likes, qLike).on(qLike.user.id.eq(userId))
                .join(album.pictures, picture)
                .leftJoin(picture.comments, comment)
                .fetchOne();

        if(!ObjectUtils.isEmpty(albumResponse.getId())) {
            List<PictureResponse> pictures = queryFactory
                    .select(Projections.constructor(PictureResponse.class,
                            picture.id,
                            picture.createdAt,
                            picture.lastModifiedAt,
                            picture.isThumbnail,
                            picture.imageUrl,
                            picture.description,
                            Projections.constructor(PlaceResponse.class,
                                    place.id,
                                    place.name,
                                    place.latitude,
                                    place.longitude,
                                    place.address),
                            ExpressionUtils.as(JPAExpressions.select(comment.countDistinct().intValue())
                                    .from(qPicture)
                                    .leftJoin(qPicture.comments, comment)
                                    .where(qPicture.id.eq(picture.id)), "commentCount")))
                    .from(picture)
                    .join(picture.place, place)
                    .where(picture.album.id.eq(albumId))
                    .fetch();

            albumResponse.setPictures(pictures);
        } else {
            albumResponse = null;
        }

        return Optional.ofNullable(albumResponse);
    }

    private BooleanExpression dateTimeLoe(LocalDateTime dateTimeLoe) {
        return dateTimeLoe != null ? album.createdAt.loe(dateTimeLoe) : null;
    }

    private BooleanExpression dateTimeGoe(LocalDateTime dateTimeGoe) {
        return dateTimeGoe != null ? album.createdAt.goe(dateTimeGoe) : null;
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? album.title.contains(title) : null;
    }

    private BooleanExpression addressNameContains(String regionDepthName) {
        return regionDepthName != null ? place.address.addressName.contains(regionDepthName) : null;
    }

    private BooleanExpression followingIdEq(Long followingId) {
        return followingId != null ? follower.to.id.eq(followingId) : null;
    }
}
