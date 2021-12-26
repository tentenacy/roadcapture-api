package com.untilled.roadcapture.api.dto.album;

import com.querydsl.core.annotations.QueryProjection;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.domain.album.Album;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
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

    public AlbumsResponse(Long id, LocalDateTime createdAt, LocalDateTime lastModifiedAt, String title, String description, String thumbnailUrl, UsersResponse user, int viewCount, int likeCount, int commentCount) {
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
