package com.untilled.roadcapture.api.dto.album;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlbumMakeUserResponse {
    private Long id;
    private String username;
    private String profileImageUrl;
}
