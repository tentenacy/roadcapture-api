package com.untilled.roadcapture.domain.picture;

import com.untilled.roadcapture.api.dto.album.AlbumUpdateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureUpdateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import com.untilled.roadcapture.api.exception.AlbumNotFoundException;
import com.untilled.roadcapture.api.exception.PictureNotFoundException;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.album.AlbumRepository;
import com.untilled.roadcapture.domain.place.Place;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PictureService {

    private final AlbumRepository albumRepository;
    private final PictureRepository pictureRepository;

    @Transactional
    public void add(Long albumId, PictureCreateRequest request) {
        getAlbumIfExists(albumId).addPicture(Picture.create(request.getImageUrl(), request.getDescription(), request.getPlace().toEntity()));
    }

    @Transactional
    public void update(Long pictureId, PictureUpdateRequest request) {
        getPictureIfExists(pictureId).update(request.getImageUrl(), request.getDescription());
    }

    @Transactional
    public void deleteEmptyPicture(Long albumId, AlbumUpdateRequest request) {

        List<Picture> foundPictures = pictureRepository.getPicturesByAlbumId(albumId)
                .orElseThrow(PictureNotFoundException::new);

        for(Picture picture : foundPictures) {
            if(!hasPictureIdOrNull(request, picture)) {
                pictureRepository.delete(picture);
            }
        }
    }

    private boolean hasPictureIdOrNull(AlbumUpdateRequest request, Picture picture) {
        for (PictureUpdateRequest pictureUpdateRequest : request.getPictures()) {
            if (pictureUpdateRequest.getId() == null || pictureUpdateRequest.getId().equals(picture.getId())) {
                return true;
            }
        }
        return false;
    }

    private Picture getPictureIfExists(Long pictureId) {
        return pictureRepository.findById(pictureId)
                .orElseThrow(PictureNotFoundException::new);
    }

    private Album getAlbumIfExists(Long albumId) {
        return albumRepository.findById(albumId).orElseThrow(AlbumNotFoundException::new);
    }
}
