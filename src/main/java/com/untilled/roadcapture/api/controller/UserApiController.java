package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.dto.base.PageRequest;
import com.untilled.roadcapture.api.dto.user.*;
import com.untilled.roadcapture.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping
    public Page<UsersResponse> users(Pageable pageable) {
        return userService.findUsers(pageable);
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
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Validated SignupRequest signupRequest) {
        userService.signup(signupRequest);
    }

    @PatchMapping("/{userId}")
    public void update(@PathVariable final Long userId, @Validated @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.update(userId, userUpdateRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long userId) {
        userService.delete(userId);
    }

}
