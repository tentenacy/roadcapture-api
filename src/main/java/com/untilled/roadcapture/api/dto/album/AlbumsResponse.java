package com.untilled.roadcapture.api.dto.album;

import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.domain.album.Album;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumsResponse {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String title;
    private String description;
    private String thumbnailUrl;
    private UsersResponse user;
    private int viewCount;
    private Integer likeCount;
    private Integer commentCount;

    public AlbumsResponse(Album album) {
        this.id = album.getId();
        this.createdAt = album.getCreatedAt();
        this.lastModifiedAt = album.getLastModifiedAt();
        this.title = album.getTitle();
        this.description = album.getDescription();
        this.thumbnailUrl = album.getThumbnailUrl();
        this.user = new UsersResponse(album.getUser());
        this.viewCount = album.getViewCount();
        this.likeCount = album.getLikes().size();
        this.commentCount = album.getComments().size();
    }
}