package com.untilled.roadcapture.api.dto.album;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AlbumResponse {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Integer viewCount;
    private UsersResponse user;
    private Integer likeCount;
    private int commentCount;
    private List<PictureResponse> pictures;

    public AlbumResponse(Album album) {
        this.id = album.getId();
        this.createdAt = album.getCreatedAt();
        this.lastModifiedAt = album.getLastModifiedAt();
        this.title = album.getTitle();
        this.description = album.getDescription();
        this.thumbnailUrl = album.getThumbnailUrl();
        this.viewCount = album.getViewCount();
        this.user = new UsersResponse(album.getUser());
        this.likeCount = album.getLikes().size();
        this.pictures = album.getPictures().stream().map(PictureResponse::new).collect(Collectors.toList());
    }
}
