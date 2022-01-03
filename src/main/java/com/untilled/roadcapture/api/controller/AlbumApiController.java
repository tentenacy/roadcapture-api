package com.untilled.roadcapture.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.untilled.roadcapture.api.dto.album.*;
import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.exception.business.CEntityMultipartSizeMismatchException;
import com.untilled.roadcapture.api.service.AlbumService;
import com.untilled.roadcapture.api.service.cloud.FileUploadService;
import com.untilled.roadcapture.util.validator.CustomCollectionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlbumApiController {

    private final CustomCollectionValidator validator;
    private final AlbumService albumService;
    private final FileUploadService fileUploadService;
    private final ObjectMapper mapper;

    @GetMapping("/albums")
    public Page<AlbumsResponse> albums(@Validated AlbumsCondition cond, Pageable pageable) {
        return albumService.getAlbums(cond, pageable);
    }

    @GetMapping("/albums/{albumId}")
    public AlbumResponse album(@PathVariable Long albumId) {
        return albumService.getAlbum(albumId);
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

    @PostMapping("/albums")
    @ResponseStatus(HttpStatus.CREATED)
    public void upload(@ModelAttribute AlbumMultiPartRequest request, BindingResult bindingResult) throws IOException, BindException {

        AlbumCreateRequest albumCreateRequest = mapper.readValue(request.getData(), AlbumCreateRequest.class);
        log.info("albumCreateRequest={}", albumCreateRequest);

        //TODO: 리팩토링
        validator.validate(albumCreateRequest, bindingResult);
        validator.validate(albumCreateRequest.getPictures(), bindingResult);
        albumCreateRequest.getPictures().stream()
                .forEach(picture -> {
                    validator.validate(picture.getPlace(), bindingResult);
                    validator.validate(picture.getPlace().getAddress(), bindingResult);
                });

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        if(albumCreateRequest.getPictures().size() != request.getImages().size()) {
            throw new CEntityMultipartSizeMismatchException();
        }

        for (PictureCreateRequest picture : albumCreateRequest.getPictures()) {
            log.info("createdAt={}", picture.getCreatedAt());
            log.info("originalFileName={}", request.getImages().get(picture.getCreatedAt()).getOriginalFilename());
            String imageUrl = fileUploadService.uploadImage(request.getImages().get(picture.getCreatedAt()));
            picture.setImageUrl(imageUrl);
        }
        albumService.create(albumCreateRequest);
    }

    @DeleteMapping("/albums/{albumId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long albumId) {
        albumService.delete(albumId);
    }
}
