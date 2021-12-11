package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.dto.base.PageRequest;
import com.untilled.roadcapture.api.dto.user.*;
import com.untilled.roadcapture.domain.user.User;
import com.untilled.roadcapture.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UsersResponse>> users(final PageRequest pageRequest) {
        Page<User> foundUsers = userService.findUsers(pageRequest);
        Page<UsersResponse> result = foundUsers.map(UsersResponse::new);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> user(@PathVariable final Long userId) {
        User foundUser = userService.findOne(userId);
        return ResponseEntity.ok(new UserResponse(foundUser));
    }

    @PostMapping
    public ResponseEntity<Void> signup(@RequestBody @Validated SignupRequest signupRequest) {
        Long userId = userService.signup(signupRequest);
        return ResponseEntity.created(URI.create("/posts/" + userId)).build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> update(@PathVariable final Long userId, @Validated @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.update(userId, userUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable final Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }


}
