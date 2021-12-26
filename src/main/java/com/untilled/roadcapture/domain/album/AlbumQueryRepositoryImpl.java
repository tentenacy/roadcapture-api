package com.untilled.roadcapture.domain.album;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.untilled.roadcapture.api.dto.album.AlbumsCondition;
import com.untilled.roadcapture.api.dto.album.AlbumsResponse;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.domain.comment.QComment;
import com.untilled.roadcapture.domain.like.QLike;
import com.untilled.roadcapture.domain.picture.QPicture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.untilled.roadcapture.domain.album.QAlbum.album;
import static com.untilled.roadcapture.domain.comment.QComment.*;
import static com.untilled.roadcapture.domain.like.QLike.*;
import static com.untilled.roadcapture.domain.picture.QPicture.*;
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
    public Page<AlbumsResponse> getAlbums(AlbumsCondition cond, Pageable pageable) {
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
                        like.count().intValue().as("likeCount"),
                        comment.count().intValue().as("commentCount")
                ))
                .from(album)
                .join(album.user, user)
                .leftJoin(album.likes, like)
                .join(album.pictures, picture)
                .leftJoin(picture.comments, comment)
                .groupBy(album.id)
                .where(
                        dateTimeLoe(cond.getDateTimeTo()),
                        dateTimeGoe(cond.getDateTimeFrom())
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
    public Optional<Album> getAlbum(Long albumId) {
        Album foundAlbum = queryFactory
                .selectFrom(album)
                .join(album.user, user).fetchJoin()
                .where(album.id.eq(albumId))
                .fetchOne();
        return Optional.ofNullable(foundAlbum);
    }

    private BooleanExpression dateTimeLoe(LocalDateTime dateTimeLoe) {
        return dateTimeLoe != null ? album.createdAt.loe(dateTimeLoe) : null;
    }

    private BooleanExpression dateTimeGoe(LocalDateTime dateTimeGoe) {
        return dateTimeGoe != null ? album.createdAt.goe(dateTimeGoe) : null;
    }
}
