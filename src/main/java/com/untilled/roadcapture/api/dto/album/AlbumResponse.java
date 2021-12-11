package com.untilled.roadcapture.api.dto.album;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlbumResponse {
    private String title;
    private String description;
    private String thumbnailUrl;
    private AlbumMakeUserResponse userAlbumResponse;
}
