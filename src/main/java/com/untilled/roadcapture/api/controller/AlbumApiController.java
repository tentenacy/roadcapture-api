package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.dto.album.*;
import com.untilled.roadcapture.api.service.AlbumService;
import com.untilled.roadcapture.util.validator.CustomCollectionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AlbumApiController {

    private final CustomCollectionValidator validator;
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
    public void create(@Validated @RequestBody AlbumCreateRequest request, BindingResult bindingResult) throws BindException {

        //TODO: 리팩토링
        validator.validate(request.getPictures(), bindingResult);
        request.getPictures().stream()
                .forEach(picture -> {
                    validator.validate(picture.getPlace(), bindingResult);
                    validator.validate(picture.getPlace().getAddress(), bindingResult);
                });

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        albumService.create(request);
    }

    @PutMapping("/albums/{albumId}")
    public void update(@PathVariable Long albumId, @Validated @RequestBody AlbumUpdateRequest request, BindingResult bindingResult) throws BindException {

        //TODO: 리팩토링
        validator.validate(request.getPictures(), bindingResult);
        request.getPictures().stream()
                        .forEach(picture -> {
                            validator.validate(picture.getPlace(), bindingResult);
                            validator.validate(picture.getPlace().getAddress(), bindingResult);
                        });

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        albumService.update(albumId, request);
    }

    @DeleteMapping("/albums/{albumId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long albumId) {
        albumService.delete(albumId);
    }
}
