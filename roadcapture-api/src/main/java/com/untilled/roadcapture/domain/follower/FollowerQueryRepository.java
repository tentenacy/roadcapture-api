package com.untilled.roadcapture.domain.follower;

import com.untilled.roadcapture.api.dto.follower.FollowersCondition;
import com.untilled.roadcapture.api.dto.follower.FollowingsCondition;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FollowerQueryRepository {
    Page<UsersResponse> getFollowings(FollowingsCondition cond, Pageable pageable, Long fromUserId);
    Page<UsersResponse> getFollowers(FollowersCondition cond, Pageable pageable, Long toUserId);
    Optional<Follower> getFollowerByFromIdAndToId(Long fromUserId, Long toUserId);
}
