package com.untilled.roadcapture.domain.follower;

import com.untilled.roadcapture.api.dto.follower.FollowersCondition;
import com.untilled.roadcapture.api.dto.follower.FollowersResponse;
import com.untilled.roadcapture.api.dto.follower.FollowingsCondition;
import com.untilled.roadcapture.api.dto.follower.FollowingsSortByAlbumResponse;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FollowerQueryRepository {
    Page<UsersResponse> getFollowings(FollowingsCondition cond, Pageable pageable, Long fromUserId);
    Page<FollowersResponse> getFollowers(FollowersCondition cond, Pageable pageable, Long toUserId);
    Optional<Follower> getFollowerByFromIdAndToId(Long fromUserId, Long toUserId);
    Page<FollowingsSortByAlbumResponse> getFollowingsSortByAlbum(Pageable pageable, Long fromUserId);
}
