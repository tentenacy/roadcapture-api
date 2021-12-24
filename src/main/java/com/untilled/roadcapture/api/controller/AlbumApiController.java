package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.dto.album.*;
import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureUpdateRequest;
import com.untilled.roadcapture.domain.album.AlbumService;
import com.untilled.roadcapture.domain.picture.PictureService;
import com.untilled.roadcapture.domain.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
public class AlbumApiController {

    private final AlbumService albumService;
    private final PictureService pictureService;
    private final PlaceService placeService;

    @GetMapping
    public Page<AlbumsResponse> albums(AlbumSearchCondition cond, Pageable pageable) {
        return albumService.getAlbums(cond, pageable);
    }

    @GetMapping("/{albumId}")
    public AlbumResponse album(@PathVariable Long albumId) {
        return albumService.getAlbum(albumId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Validated @RequestBody AlbumCreateRequest request) {
        albumService.create(request);
    }

    @PutMapping("/{albumId}")
    public void update(@PathVariable Long albumId, @Validated @RequestBody AlbumUpdateRequest request) {
        //Album 중 request에 대응되는 Picture가 없다면 해당 Picture 삭제
        pictureService.deleteEmptyPicture(albumId, request);
        for(PictureUpdateRequest pictureUpdateRequest : request.getPictures()) {
            //request에 id가 없는 Picture가 있다면 해당 Picture 앨범에 추가
            if(pictureUpdateRequest.getId() == null) {
                pictureService.add(albumId, new PictureCreateRequest(pictureUpdateRequest));
            }
            //없다면 해당 Picture 및 Place 업데이트
            else {
                placeService.update(pictureUpdateRequest.getPlace().getId(), pictureUpdateRequest.getPlace());
                pictureService.update(pictureUpdateRequest.getId(), pictureUpdateRequest);
            }
        }
        //Album 업데이트
        albumService.update(albumId, request);
    }

    @DeleteMapping("/{albumId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long albumId) {
        albumService.delete(albumId);
    }
}
