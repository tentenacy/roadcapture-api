package com.untilled.roadcapture.api.dto.album;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowingAlbumsCondition {

    private Long followingId;
}
