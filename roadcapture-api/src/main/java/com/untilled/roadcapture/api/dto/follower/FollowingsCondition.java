package com.untilled.roadcapture.api.dto.follower;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowingsCondition {

    private String username;
}
