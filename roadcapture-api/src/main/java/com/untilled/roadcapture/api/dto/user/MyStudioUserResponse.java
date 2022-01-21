package com.untilled.roadcapture.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyStudioUserResponse {
    private Long id;
    private String username;
    private String profileImageUrl;
    private String backgroundImageUrl;
    private String introduction;
    private int followerCount;
    private int followingCount;
}
