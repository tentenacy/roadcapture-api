package com.untilled.roadcapture.api.dto.follower;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowersResponse {
    private Long id;
    private String username;
    private String profileImageUrl;
    private boolean followed;
}
