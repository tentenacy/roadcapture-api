package com.untilled.roadcapture.api.dto.follower;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FollowingsSortByAlbumResponse {
    private Long id;
    private String username;
    private String profileImageUrl;
    private LocalDateTime latestAlbumCreatedAt;
    private LocalDateTime latestAlbumLastModifiedAt;
}
