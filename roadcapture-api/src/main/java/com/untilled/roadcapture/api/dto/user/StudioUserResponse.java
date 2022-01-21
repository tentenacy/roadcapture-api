package com.untilled.roadcapture.api.dto.user;

import com.untilled.roadcapture.domain.place.Place;
import com.untilled.roadcapture.domain.user.User;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class StudioUserResponse {
    private Long id;
    private String username;
    private String profileImageUrl;
    private String backgroundImageUrl;
    private String introduction;
    private int followerCount;
    private int followingCount;
    private boolean followed;
}
