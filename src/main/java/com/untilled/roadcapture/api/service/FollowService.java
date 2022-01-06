package com.untilled.roadcapture.api.service;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
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
    public void follow(Long toUserId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = getUserIfExists(user.getId());
        User userToFollow = getUserToFollowIfExists(toUserId);

        checkFollowMyselfException(foundUser, userToFollow);

        followerRepository.save(Follower.create(foundUser, userToFollow));
    }

    private void checkFollowMyselfException(User foundUser, User userToFollow) {
        if(foundUser.equals(userToFollow)) {
            throw new CInvalidValueException(ErrorCode.FOLLOW_MYSELF_ERROR);
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
