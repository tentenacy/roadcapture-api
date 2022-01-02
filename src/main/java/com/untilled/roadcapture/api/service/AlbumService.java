package com.untilled.roadcapture.api.service;

import com.untilled.roadcapture.api.dto.album.*;
import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureUpdateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceUpdateRequest;
import com.untilled.roadcapture.api.exception.business.*;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.album.AlbumRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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
        return albumRepository.search(cond, pageable);
    }

    public AlbumResponse getAlbum(Long albumId) {
        return albumRepository.getAlbum(albumId).orElseThrow(CAlbumNotFoundException::new);
    }

    @Transactional
    public Album create(AlbumCreateRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Picture> pictures = request.getPictures().stream()
                .map(pictureCreateRequest -> pictureCreateRequest.toEntity())
                .collect(Collectors.toList());

        return albumRepository.save(Album.create(
                request.getTitle(),
                request.getDescription(),
                request.getThumbnailUrl(),
                pictures,
                getUserIfExists(user.getId())
        ));
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

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Album foundAlbum = getAlbumIfExists(albumId);

        List<Long> requestPictureIds = request.getPictures().stream()
                .map(PictureUpdateRequest::getId).collect(Collectors.toList());

        //유저의 앨범이 아니면 예외
        checkUserOwnAlbum(user, foundAlbum);

        //사진이 해당 앨범에 속하지 않으면 예외
        checkPictureBelong(foundAlbum, requestPictureIds);

        //요청에 없는 사진 삭제
        foundAlbum.removeAllPicturesExceptFor(requestPictureIds);

        for (PictureUpdateRequest pictureUpdateRequest : request.getPictures()) {

            PlaceUpdateRequest placeUpdateRequest = pictureUpdateRequest.getPlace();

            //request에 id가 없는 Picture가 있다면 해당 Picture 앨범에 추가
            if (ObjectUtils.isEmpty(pictureUpdateRequest.getId())) {

                PictureCreateRequest pictureCreateRequest = new PictureCreateRequest(pictureUpdateRequest);

                foundAlbum.addPicture(Picture.create(
                        pictureCreateRequest.getImageUrl(),
                        pictureCreateRequest.getDescription(),
                        pictureCreateRequest.getPlace().toEntity())
                );
            }
            //없다면 해당 Picture 및 Place 업데이트
            else {

                Picture foundPicture = getPictureIfExists(pictureUpdateRequest.getId());

                getPlaceIfExists(foundPicture.getPlace().getId()).update(
                        placeUpdateRequest.getName(),
                        placeUpdateRequest.getLatitude(),
                        placeUpdateRequest.getLongitude(),
                        placeUpdateRequest.getAddress()
                );

                foundPicture.update(
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

    private void checkPictureBelong(Album foundAlbum, List<Long> requestPictureIds) {
        requestPictureIds.stream()
                .forEach(pictureId -> {
                    if (pictureId != null && !foundAlbum.getPictures().stream().anyMatch(picture -> picture.getId().equals(pictureId)))
                        throw new CPictureBelongException();
                });
    }

    private void checkUserOwnAlbum(User user, Album foundAlbum) {
        if (!user.getId().equals(foundAlbum.getUser().getId())) {
            throw new CUserOwnAlbumException();
        }
    }

    @Transactional
    public void delete(Long albumId) {
        albumRepository.delete(getAlbumIfExists(albumId));
    }

    private Place getPlaceIfExists(Long placeId) {
        return placeRepository.findById(placeId).orElseThrow(CPlaceNotFoundException::new);
    }

    private Album getAlbumIfExists(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(CAlbumNotFoundException::new);
    }

    private Picture getPictureIfExists(Long pictureId) {
        return pictureRepository.findById(pictureId)
                .orElseThrow(CPictureNotFoundException::new);
    }

    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(CUserNotFoundException::new);
    }

}
