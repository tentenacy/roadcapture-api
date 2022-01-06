package com.untilled.roadcapture.api.service;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.dto.follower.FollowersCondition;
import com.untilled.roadcapture.api.dto.follower.FollowingsCondition;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException.CUserNotFoundException;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException.CUserToFollowNotFoundException;
import com.untilled.roadcapture.api.exception.business.CInvalidValueException;
import com.untilled.roadcapture.domain.follower.Follower;
import com.untilled.roadcapture.domain.follower.FollowerRepository;
import com.untilled.roadcapture.domain.user.User;
import com.untilled.roadcapture.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.untilled.roadcapture.api.exception.business.CEntityNotFoundException.*;
import static com.untilled.roadcapture.api.exception.business.CInvalidValueException.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowerService {

    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    @Transactional
    public void create(Long fromUserId, Long toUserId) {

        User foundUser = getUserIfExists(fromUserId);
        User foundUserToFollow = getUserToFollowIfExists(toUserId);

        checkFollowMyselfException(foundUser, foundUserToFollow);

        followerRepository.save(Follower.create(foundUser, foundUserToFollow));
    }

    @Transactional
    public void delete(Long fromUserId, Long toUserId) {

        User foundUser = getUserIfExists(fromUserId);
        User foundUserToUnFollow = getUserToFollowIfExists(toUserId);

        checkUnFollowMyselfException(foundUser, foundUserToUnFollow);

        followerRepository.delete(getFollowerIfExists(foundUser.getId(), foundUserToUnFollow.getId()));
    }

    public Page<UsersResponse> getFollowings(FollowingsCondition cond, Pageable pageable, Long userId) {
        return followerRepository.getFollowings(cond, pageable, getUserIfExists(userId).getId());
    }

    public Page<UsersResponse> getFollowers(FollowersCondition cond, Pageable pageable, Long userId) {
        return followerRepository.getFollowers(cond, pageable, getUserIfExists(userId).getId());
    }

    private void checkFollowMyselfException(User foundUser, User userToFollow) {
        if(foundUser.equals(userToFollow)) {
            throw new CFollowMyselfException();
        }
    }

    private void checkUnFollowMyselfException(User foundUser, User userToFollow) {
        if(foundUser.equals(userToFollow)) {
            throw new CUnfollowMyselfException();
        }
    }

    private Follower getFollowerIfExists(Long fromUserId, Long toUserId) {
        return followerRepository.getFollowerByFromUserIdAndToUserId(fromUserId, toUserId)
                .orElseThrow(CFollowerNotFoundException::new);
    }

    private User getUserToFollowIfExists(Long toUserId) {
        return userRepository.findById(toUserId)
                .orElseThrow(CUserToFollowNotFoundException::new);
    }

    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(CUserNotFoundException::new);
    }
}
