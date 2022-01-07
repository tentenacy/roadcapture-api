package com.untilled.roadcapture.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlbumUserResponse {
    private Long id;
    private String username;
    private String profileImageUrl;
    private boolean followed;
}
