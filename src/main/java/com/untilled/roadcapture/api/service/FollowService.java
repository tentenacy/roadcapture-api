package com.untilled.roadcapture.api.service;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException;
import com.untilled.roadcapture.api.exception.business.CInvalidValueException;
import com.untilled.roadcapture.api.exception.business.CUserNotFoundException;
import com.untilled.roadcapture.api.exception.business.CUserToFollowNotFoundException;
import com.untilled.roadcapture.domain.follower.Follower;
import com.untilled.roadcapture.domain.follower.FollowerRepository;
import com.untilled.roadcapture.domain.user.User;
import com.untilled.roadcapture.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    @Transactional
    public void create(Long toUserId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = getUserIfExists(user.getId());
        User foundUserToFollow = getUserToFollowIfExists(toUserId);

        checkFollowMyselfException(foundUser, foundUserToFollow);

        followerRepository.save(Follower.create(foundUser, foundUserToFollow));
    }

    @Transactional
    public void delete(Long toUserId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = getUserIfExists(user.getId());
        User foundUserToUnFollow = getUserToFollowIfExists(toUserId);

        checkUnFollowMyselfException(foundUser, foundUserToUnFollow);

        followerRepository.delete(getFollowerIfExists(foundUser, foundUserToUnFollow));
    }

    private Follower getFollowerIfExists(User foundUser, User foundUserToUnFollow) {
        return followerRepository.findByFromAndTo(foundUser, foundUserToUnFollow)
                .orElseThrow(() -> new CEntityNotFoundException(ErrorCode.FOLLOWER_NOT_FOUND));
    }

    private void checkFollowMyselfException(User foundUser, User userToFollow) {
        if(foundUser.equals(userToFollow)) {
            throw new CInvalidValueException(ErrorCode.FOLLOW_MYSELF_ERROR);
        }
    }

    private void checkUnFollowMyselfException(User foundUser, User userToFollow) {
        if(foundUser.equals(userToFollow)) {
            throw new CInvalidValueException(ErrorCode.UNFOLLOW_MYSELF_ERROR);
        }
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
