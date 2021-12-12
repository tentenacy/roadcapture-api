package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.dto.base.PageRequest;
import com.untilled.roadcapture.api.dto.user.*;
import com.untilled.roadcapture.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping
    public Page<UsersResponse> users(final PageRequest pageRequest) {
        return userService.findUsers(pageRequest).map(UsersResponse::new);
    }

    @GetMapping("/{userId}")
    public UserResponse user(@PathVariable final Long userId) {
        return new UserResponse(userService.findOne(userId));
    }

    @GetMapping("/{userId}/details")
    public UserDetailResponse userDetails(@PathVariable final Long userId) {
        return new UserDetailResponse(userService.findOne(userId));
    }

    @PostMapping
    public void signup(@RequestBody @Validated SignupRequest signupRequest) {
        userService.signup(signupRequest);
    }

    @PatchMapping("/{userId}")
    public void update(@PathVariable final Long userId, @Validated @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.update(userId, userUpdateRequest);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable final Long userId) {
        userService.delete(userId);
    }


}
