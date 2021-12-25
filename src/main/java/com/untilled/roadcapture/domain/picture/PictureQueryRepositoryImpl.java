package com.untilled.roadcapture.domain.picture;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.album.QAlbum;
import com.untilled.roadcapture.domain.place.QPlace;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

import static com.untilled.roadcapture.domain.album.QAlbum.*;
import static com.untilled.roadcapture.domain.picture.QPicture.*;
import static com.untilled.roadcapture.domain.place.QPlace.*;

public class PictureQueryRepositoryImpl extends QuerydslRepositorySupport implements PictureQueryRepository {

    private final JPAQueryFactory queryFactory;

    public PictureQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Picture.class);
        this.queryFactory = queryFactory;
    }
}
