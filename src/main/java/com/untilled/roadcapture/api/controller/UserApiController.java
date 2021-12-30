package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.dto.token.TokenRequest;
import com.untilled.roadcapture.api.dto.token.TokenResponse;
import com.untilled.roadcapture.api.dto.user.*;
import com.untilled.roadcapture.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public Page<UsersResponse> users(Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @GetMapping("/{userId}")
    public UserResponse user(@PathVariable final Long userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/{userId}/details")
    public UserDetailResponse userDetail(@PathVariable final Long userId) {
        return userService.getUserDetail(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Validated SignupRequest signupRequest) {
        userService.signup(signupRequest);
    }

    @PostMapping("/tokens")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse login(@RequestBody @Validated LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/tokens/reissue")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse reissue(@RequestBody @Validated TokenRequest request) {
        return userService.reissue(request);
    }

    @PatchMapping("/{userId}")
    public void update(@PathVariable final Long userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.update(userId, userUpdateRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long userId) {
        userService.delete(userId);
    }

}
