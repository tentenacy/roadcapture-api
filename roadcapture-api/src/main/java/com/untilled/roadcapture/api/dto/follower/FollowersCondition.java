package com.untilled.roadcapture.api.dto.follower;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowersCondition {

    private String username;
}
