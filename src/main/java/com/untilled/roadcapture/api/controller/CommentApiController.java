package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.dto.comment.CommentCreateRequest;
import com.untilled.roadcapture.domain.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/users/{userId}/albums/{albumId}/pictures/{pictureId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable Long userId,
                       @PathVariable Long albumId,
                       @PathVariable Long pictureId,
                       @RequestBody @Validated CommentCreateRequest request) {
        log.info("userId={}, albumId={}, pictureId={}", userId, albumId, pictureId);
        log.info("request={}", request.getContent());
        commentService.create(userId, pictureId, request);
    }

    @DeleteMapping("/pictures/{pictureId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long pictureId,
                       @PathVariable Long commentId) {
        commentService.delete(pictureId, commentId);
    }
}
