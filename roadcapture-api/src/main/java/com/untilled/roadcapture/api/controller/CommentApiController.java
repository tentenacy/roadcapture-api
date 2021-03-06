package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.dto.comment.CommentCreateRequest;
import com.untilled.roadcapture.api.dto.comment.CommentsResponse;
import com.untilled.roadcapture.api.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/pictures/{pictureId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable Long pictureId,
                       @RequestBody @Validated CommentCreateRequest request) {
        commentService.create(pictureId, request);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }

    @GetMapping("/pictures/{pictureId}/comments")
    public Page<CommentsResponse> pictureComments(@PathVariable Long pictureId, Pageable pageable) {
        return commentService.pictureComments(pictureId, pageable);
    }

    @GetMapping("/albums/{albumId}/pictures/comments")
    public Page<CommentsResponse> albumComments(@PathVariable Long albumId, Pageable pageable) {
        return commentService.albumComments(albumId, pageable);
    }
}
