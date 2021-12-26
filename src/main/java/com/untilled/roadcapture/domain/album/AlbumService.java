package com.untilled.roadcapture.domain.album;

import com.untilled.roadcapture.api.dto.album.*;
import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureUpdateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceUpdateRequest;
import com.untilled.roadcapture.api.exception.AlbumNotFoundException;
import com.untilled.roadcapture.api.exception.PictureNotFoundException;
import com.untilled.roadcapture.api.exception.PlaceNotFoundException;
import com.untilled.roadcapture.api.exception.UserNotFoundException;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.picture.PictureRepository;
import com.untilled.roadcapture.domain.place.Place;
import com.untilled.roadcapture.domain.place.PlaceRepository;
import com.untilled.roadcapture.domain.user.User;
import com.untilled.roadcapture.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;
    private final PlaceRepository placeRepository;

    public Page<AlbumsResponse> getAlbums(AlbumsCondition cond, Pageable pageable) {
        return albumRepository.getAlbums(cond, pageable);
    }

    public AlbumResponse getAlbum(Long albumId) {
        return albumRepository.getAlbum(albumId).orElseThrow(AlbumNotFoundException::new);
    }

    @Transactional
    public Album create(Long userId, AlbumCreateRequest request) {

        List<Picture> pictures = request.getPictures().stream()
                .map(pictureCreateRequest -> pictureCreateRequest.toEntity())
                .collect(Collectors.toList());

        return albumRepository.save(Album.create(
                request.getTitle(),
                request.getDescription(),
                request.getThumbnailUrl(),
                pictures,
                getUserIfExists(userId)
        ));
    }

    @Transactional
    public void update(Long albumId, AlbumUpdateRequest request) {

        Album foundAlbum = getAlbumIfExists(albumId);

        foundAlbum.removeAllPicturesExceptFor(request.getPictures().stream()
                .map(PictureUpdateRequest::getId).collect(Collectors.toList()));

        for(PictureUpdateRequest pictureUpdateRequest : request.getPictures()) {
            PlaceUpdateRequest placeUpdateRequest = pictureUpdateRequest.getPlace();

            //request에 id가 없는 Picture가 있다면 해당 Picture 앨범에 추가
            if(pictureUpdateRequest.getId() == null) {
                PictureCreateRequest pictureCreateRequest = new PictureCreateRequest(pictureUpdateRequest);
                foundAlbum.addPicture(Picture.create(
                                pictureCreateRequest.getImageUrl(),
                                pictureCreateRequest.getDescription(),
                                pictureCreateRequest.getPlace().toEntity())
                        );
            }
            //없다면 해당 Picture 및 Place 업데이트
            else {
                getPlaceIfExists(placeUpdateRequest.getId()).update(
                        placeUpdateRequest.getName(),
                        placeUpdateRequest.getLatitude(),
                        placeUpdateRequest.getLongitude(),
                        placeUpdateRequest.getAddress()
                );
                getPictureIfExists(pictureUpdateRequest.getId()).update(
                        pictureUpdateRequest.getImageUrl(),
                        pictureUpdateRequest.getDescription()
                );
            }
            //Album 업데이트
            foundAlbum
                    .update(
                            request.getTitle(),
                            request.getDescription(),
                            request.getThumbnailUrl()
                    );
        }
    }

    @Transactional
    public void delete(Long albumId) {
        albumRepository.delete(getAlbumIfExists(albumId));
    }

    private Place getPlaceIfExists(Long placeId) {
        return placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
    }

    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private Album getAlbumIfExists(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(AlbumNotFoundException::new);
    }

    private Picture getPictureIfExists(Long pictureId) {
        return pictureRepository.findById(pictureId)
                .orElseThrow(PictureNotFoundException::new);
    }

}
