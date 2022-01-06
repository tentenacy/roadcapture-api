package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.business.CInvalidValueException;
import com.untilled.roadcapture.api.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/followers")
@RequiredArgsConstructor
public class FollowerApiController {

    private final FollowService followService;

    @PostMapping("/{toUserId}")
    public void create(@PathVariable Long toUserId) {
        try {
            followService.create(toUserId);
        } catch (DataIntegrityViolationException e) {
            throw new CInvalidValueException(ErrorCode.ALREADY_FOLLOW);
        }
    }

    @DeleteMapping("/{toUserId}")
    public void delete(@PathVariable Long toUserId) {
        followService.delete(toUserId);
    }
}
