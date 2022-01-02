package com.untilled.roadcapture.domain.album;

import com.querydsl.core.QueryResults;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.group.GroupByList;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.untilled.roadcapture.api.dto.album.AlbumResponse;
import com.untilled.roadcapture.api.dto.album.AlbumsCondition;
import com.untilled.roadcapture.api.dto.album.AlbumsResponse;
import com.untilled.roadcapture.api.dto.album.QAlbumResponse;
import com.untilled.roadcapture.api.dto.picture.PictureResponse;
import com.untilled.roadcapture.api.dto.place.PlaceResponse;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.address.QAddress;
import com.untilled.roadcapture.domain.comment.QComment;
import com.untilled.roadcapture.domain.like.QLike;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.picture.QPicture;
import com.untilled.roadcapture.domain.place.QPlace;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.untilled.roadcapture.domain.address.QAddress.*;
import static com.untilled.roadcapture.domain.album.QAlbum.album;
import static com.untilled.roadcapture.domain.comment.QComment.*;
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
    public Page<AlbumsResponse> search(AlbumsCondition cond, Pageable pageable) {
        JPAQuery<AlbumsResponse> query = queryFactory
                .select(Projections.constructor(AlbumsResponse.class,
                        album.id,
                        album.createdAt,
                        album.lastModifiedAt,
                        album.title,
                        album.description,
                        album.thumbnailUrl,
                        Projections.constructor(UsersResponse.class,
                                user.id,
                                user.username,
                                user.profileImageUrl),
                        album.viewCount,
                        like.countDistinct().intValue().as("likeCount"),
                        comment.countDistinct().intValue().as("commentCount")
                ))
                .from(album)
                .join(album.user, user)
                .leftJoin(album.likes, like)
                .join(album.pictures, picture)
                .leftJoin(picture.comments, comment)
                .groupBy(album.id)
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

        //카운트 쿼리 필요에 따라 날라감
        return new PageImpl(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Optional<AlbumResponse> getAlbum(Long albumId) {
        AlbumResponse albumResponse = queryFactory
                .select(new QAlbumResponse(
                        QAlbum.album.id,
                        QAlbum.album.createdAt,
                        QAlbum.album.lastModifiedAt,
                        QAlbum.album.title,
                        QAlbum.album.description,
                        QAlbum.album.thumbnailUrl,
                        Projections.constructor(UsersResponse.class,
                                user.id,
                                user.username,
                                user.profileImageUrl),
                        QAlbum.album.viewCount,
                        like.countDistinct().intValue().as("likeCount"),
                        comment.countDistinct().intValue().as("commentCount")))
                .from(QAlbum.album)
                .join(QAlbum.album.user, user)
                .leftJoin(QAlbum.album.likes, like)
                .join(QAlbum.album.pictures, picture)
                .leftJoin(picture.comments, comment)
                .where(QAlbum.album.id.eq(albumId))
                .fetchOne();

        if(!ObjectUtils.isEmpty(albumResponse.getId())) {
            List<PictureResponse> pictures = queryFactory
                    .select(Projections.constructor(PictureResponse.class,
                            picture.id,
                            picture.createdAt,
                            picture.lastModifiedAt,
                            picture.imageUrl,
                            picture.description,
                            Projections.constructor(PlaceResponse.class,
                                    place.id,
                                    place.name,
                                    place.latitude,
                                    place.longitude,
                                    place.address)))
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
}
