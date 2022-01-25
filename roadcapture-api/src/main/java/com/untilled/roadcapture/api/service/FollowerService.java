package com.untilled.roadcapture.api.service;

import com.untilled.roadcapture.api.dto.follower.FollowersCondition;
import com.untilled.roadcapture.api.dto.follower.FollowingsCondition;
import com.untilled.roadcapture.api.dto.follower.FollowingsSortByAlbumResponse;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException.CUserNotFoundException;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException.CUserToFollowNotFoundException;
import com.untilled.roadcapture.domain.follower.Follower;
import com.untilled.roadcapture.domain.follower.FollowerRepository;
import com.untilled.roadcapture.domain.user.User;
import com.untilled.roadcapture.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        User foundUser = getUserThrowable(fromUserId);
        User foundUserToFollow = getUserToFollowThrowable(toUserId);

        checkFollowMyselfException(foundUser, foundUserToFollow);

        followerRepository.save(Follower.create(foundUser, foundUserToFollow));
    }

    @Transactional
    public void delete(Long fromUserId, Long toUserId) {

        checkUnFollowMyselfException(getUserThrowable(fromUserId), getUserToFollowThrowable(toUserId));

        followerRepository.delete(getFollowerIfExists(fromUserId, toUserId));
    }

    public Page<UsersResponse> getFollowings(FollowingsCondition cond, Pageable pageable, Long fromUserId) {
        return followerRepository.getFollowings(cond, pageable, getUserThrowable(fromUserId).getId());
    }

    public Page<UsersResponse> getFollowers(FollowersCondition cond, Pageable pageable, Long fromUserId) {
        return followerRepository.getFollowers(cond, pageable, getUserThrowable(fromUserId).getId());
    }

    public Page<FollowingsSortByAlbumResponse> getFollowingsSortByAlbum(Pageable pageable, Long fromUserId) {
        return followerRepository.getFollowingsSortByAlbum(pageable, fromUserId);
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
        return followerRepository.getFollowerByFromIdAndToId(fromUserId, toUserId)
                .orElseThrow(CFollowerNotFoundException::new);
    }

    private User getUserToFollowThrowable(Long toUserId) {
        return userRepository.findById(toUserId)
                .orElseThrow(CUserToFollowNotFoundException::new);
    }

    private User getUserThrowable(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(CUserNotFoundException::new);
    }
}
