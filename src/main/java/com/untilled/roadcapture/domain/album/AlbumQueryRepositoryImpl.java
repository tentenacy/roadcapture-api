package com.untilled.roadcapture.domain.album;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.untilled.roadcapture.api.dto.album.AlbumSearchCondition;
import com.untilled.roadcapture.api.dto.album.AlbumsResponse;
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
    public Page<AlbumsResponse> search(AlbumSearchCondition cond, Pageable pageable) {
        log.info("offset={}, limit={}", pageable.getOffset(), pageable.getPageSize());
        log.info("loe={}, goe={}", cond.getDateTimeTo(), cond.getDateTimeFrom());
        JPAQuery<Album> query = queryFactory
                .selectFrom(album)
                .join(album.user, user).fetchJoin()
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

        QueryResults<Album> result = query.fetchResults();

        //카운트 쿼리 필요에 따라 날라감
        return new PageImpl(result.getResults().stream().map(AlbumsResponse::new).collect(Collectors.toList()), pageable, result.getTotal());
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
