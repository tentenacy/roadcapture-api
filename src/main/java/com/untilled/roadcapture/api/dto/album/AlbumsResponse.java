package com.untilled.roadcapture.api.dto.album;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.domain.album.Album;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AlbumsResponse {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String title;
    private String description;
    private String thumbnailUrl;
    private UsersResponse user;
    private int viewCount;
    private int likeCount;
    private int commentCount;
    private boolean doesLike;
}
