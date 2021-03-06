package com.untilled.roadcapture.domain.user;

import com.untilled.roadcapture.api.dto.user.StudioUserResponse;
import com.untilled.roadcapture.api.dto.user.UsersCondition;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserQueryRepository {
    Page<UsersResponse> search(Pageable pageable, UsersCondition cond);
    Optional<StudioUserResponse> studioUser(Long userId, Long studioUserId);
}
