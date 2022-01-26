package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.dto.follower.FollowersCondition;
import com.untilled.roadcapture.api.dto.follower.FollowersResponse;
import com.untilled.roadcapture.api.dto.follower.FollowingsCondition;
import com.untilled.roadcapture.api.dto.follower.FollowingsSortByAlbumResponse;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.api.exception.business.CInvalidValueException;
import com.untilled.roadcapture.api.service.FollowerService;
import com.untilled.roadcapture.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FollowerApiController {

    private final FollowerService followService;

    @PostMapping("/followers/{toUserId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable Long toUserId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            followService.create(user.getId(), toUserId);
        } catch (DataIntegrityViolationException e) {
            throw new CInvalidValueException(ErrorCode.ALREADY_FOLLOW);
        }
    }

    @DeleteMapping("/followers/{toUserId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long toUserId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        followService.delete(user.getId(), toUserId);
    }

    @GetMapping("/followers/to")
    public Page<UsersResponse> followings(FollowingsCondition cond, Pageable pageable) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return followService.getFollowings(cond, pageable, user.getId());
    }

    @GetMapping("/followers/from")
    public Page<FollowersResponse> followers(FollowersCondition cond, Pageable pageable) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return followService.getFollowers(cond, pageable, user.getId());
    }

    @GetMapping("/users/{userId}/followers/to")
    public Page<UsersResponse> userFollowings(FollowingsCondition cond, Pageable pageable, @PathVariable Long userId) {

        return followService.getFollowings(cond, pageable, userId);
    }

    @GetMapping("/users/{userId}/followers/from")
    public Page<FollowersResponse> userFollowers(FollowersCondition cond, Pageable pageable, @PathVariable Long userId) {

        return followService.getFollowers(cond, pageable, userId);
    }

    @GetMapping("/followers/to/sort-by-album")
    public Page<FollowingsSortByAlbumResponse> followingsSortByAlbum(Pageable pageable) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return followService.getFollowingsSortByAlbum(pageable, user.getId());
    }
}
