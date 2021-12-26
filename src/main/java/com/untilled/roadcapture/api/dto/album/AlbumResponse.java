package com.untilled.roadcapture.api.dto.album;

import com.querydsl.core.annotations.QueryProjection;
import com.untilled.roadcapture.api.dto.picture.PictureResponse;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AlbumResponse {

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
    private List<PictureResponse> pictures;

    @QueryProjection
    public AlbumResponse(Long id, LocalDateTime createdAt, LocalDateTime lastModifiedAt, String title, String description, String thumbnailUrl, UsersResponse user, int viewCount, int likeCount, int commentCount) {
        this.id = id;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.user = user;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
