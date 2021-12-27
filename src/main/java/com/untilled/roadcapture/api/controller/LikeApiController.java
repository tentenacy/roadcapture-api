package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.domain.like.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LikeApiController {

    private final LikeService likeService;

    @PostMapping("/users/{userId}/albums/{albumId}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable Long userId, @PathVariable Long albumId) {
        likeService.create(userId, albumId);
    }

    @DeleteMapping("/users/{userId}/albums/{albumId}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long albumId) {
        likeService.delete(userId, albumId);
    }
}
