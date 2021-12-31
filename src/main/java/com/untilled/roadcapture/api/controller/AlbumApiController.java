package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.dto.album.*;
import com.untilled.roadcapture.api.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AlbumApiController {

    private final AlbumService albumService;

    @GetMapping("/albums")
    public Page<AlbumsResponse> albums(@Validated AlbumsCondition cond, Pageable pageable) {
        return albumService.getAlbums(cond, pageable);
    }

    @GetMapping("/albums/{albumId}")
    public AlbumResponse album(@PathVariable Long albumId) {
        return albumService.getAlbum(albumId);
    }

    @PostMapping("/albums")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Validated @RequestBody AlbumCreateRequest request) {
        albumService.create(request);
    }

    @PutMapping("/albums/{albumId}")
    public void update(@PathVariable Long albumId, @Validated @RequestBody AlbumUpdateRequest request) {
        albumService.update(albumId, request);
    }

    @DeleteMapping("/albums/{albumId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long albumId) {
        albumService.delete(albumId);
    }
}
