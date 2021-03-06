package com.untilled.roadcapture.api.service;

import com.untilled.roadcapture.api.dto.album.*;
import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureUpdateRequest;
import com.untilled.roadcapture.api.dto.picture.TempPictureCreateRequest;
import com.untilled.roadcapture.api.dto.picture.TempPictureUpdateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceUpdateRequest;
import com.untilled.roadcapture.api.exception.business.CEntityBelongException.CPictureBelongException;
import com.untilled.roadcapture.api.exception.business.CEntityBelongException.CUserOwnAlbumException;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.album.AlbumRepository;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.picture.PictureRepository;
import com.untilled.roadcapture.domain.place.Place;
import com.untilled.roadcapture.domain.place.PlaceRepository;
import com.untilled.roadcapture.domain.user.User;
import com.untilled.roadcapture.domain.user.UserRepository;
import com.untilled.roadcapture.util.CUrlUtils;
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

import static com.untilled.roadcapture.api.exception.business.CEntityNotFoundException.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;
    private final PlaceRepository placeRepository;

    public Page<AlbumsResponse> getAlbums(AlbumsCondition cond, Pageable pageable, Long userId) {

        getUserThrowable(userId);

        return albumRepository.getAlbums(cond, pageable, userId);
    }

    public Page<AlbumsResponse> getFollowingAlbums(FollowingAlbumsCondition cond, Pageable pageable, Long userId) {

        getUserThrowable(userId);

        return albumRepository.getFollowingAlbums(cond, pageable, userId);
    }

    public AlbumResponse getAlbum(Long albumId, Long userId) {

        getUserThrowable(userId);

        return albumRepository.getAlbum(albumId, userId).orElseThrow(CAlbumNotFoundException::new);
    }

    public Page<StudioAlbumsResponse> getMyStudioAlbums(Long userId, Pageable pageable) {
        return getStudioAlbums(userId, pageable);
    }

    public Page<StudioAlbumsResponse> getStudioAlbums(Long userId, Pageable pageable) {
        return albumRepository.getStudioAlbums(pageable, getUserThrowable(userId).getId());
    }

    @Transactional
    public Album create(AlbumCreateRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return albumRepository.save(Album.create(
                request.getTitle(),
                request.getDescription(),
                request.getPictures().stream().map(picture -> picture.toEntity()).collect(Collectors.toList()),
                getUserThrowable(user.getId())
        ));
    }

    @Transactional
    public Album tempCreate(TempAlbumCreateRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return albumRepository.save(Album.create(
                request.getTitle(),
                request.getDescription(),
                request.getPictures().stream().map(TempPictureCreateRequest::toEntity).collect(Collectors.toList()),
                getUserThrowable(user.getId())
        ));
    }

    @Transactional
    public Album create(Long userId, TempAlbumCreateRequest request) {

        List<Picture> pictures = request.getPictures().stream()
                .map(TempPictureCreateRequest::toEntity)
                .collect(Collectors.toList());

        return albumRepository.save(Album.create(
                request.getTitle(),
                request.getDescription(),
                pictures,
                getUserThrowable(userId)
        ));
    }

    @Transactional
    public List<String> update(Long albumId, AlbumUpdateRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Album foundAlbum = getAlbumThrowable(albumId);

        List<Long> requestPictureIds = request.getPictures().stream()
                .map(PictureUpdateRequest::getId).collect(Collectors.toList());

        //????????? ????????? ??? ??????
        checkUserOwnAlbum(user, foundAlbum);

        //????????? ?????? ????????? ????????? ??? ??????
        checkPictureBelong(foundAlbum, requestPictureIds);

        //????????? ?????? ?????? ??????
        List<String> fileNamesToDelete = foundAlbum.removeAllPicturesExceptFor(requestPictureIds).stream()
                .map(picture -> CUrlUtils.extractFileNameFrom(picture.getImageUrl()))
                .collect(Collectors.toList());

        for (PictureUpdateRequest picture : request.getPictures()) {

            //request??? id??? ?????? ????????? ????????? ?????? ?????? ????????? ??????
            if (ObjectUtils.isEmpty(picture.getId())) {
                foundAlbum.addPicture(new PictureCreateRequest(picture).toEntity());
            }
            //????????? ?????? Picture ??? Place ????????????
            else {
                Picture foundPicture = getPictureThrowable(picture.getId());
                getPlaceThrowable(foundPicture.getPlace().getId()).update(picture.getPlace().toEntity());

                //????????? ?????? ????????? ????????? ???????????? ?????? ??????
                if(picture.isImageUrlNotUpdatable()) picture.updateImageUrl(foundPicture.getImageUrl());
                else fileNamesToDelete.add(CUrlUtils.extractFileNameFrom(foundPicture.getImageUrl()));
                foundPicture.update(picture.toEntity());
            }

            //Album ????????????
            foundAlbum.update(request.toEntity());
        }

        return fileNamesToDelete;
    }

    @Transactional
    public void tempUpdate(Long albumId, TempAlbumUpdateRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Album foundAlbum = getAlbumThrowable(albumId);

        List<Long> requestPictureIds = request.getPictures().stream()
                .map(TempPictureUpdateRequest::getId).collect(Collectors.toList());

        //????????? ????????? ????????? ??????
        checkUserOwnAlbum(user, foundAlbum);

        //????????? ?????? ????????? ????????? ????????? ??????
        checkPictureBelong(foundAlbum, requestPictureIds);

        //????????? ?????? ?????? ??????
        foundAlbum.removeAllPicturesExceptFor(requestPictureIds);

        for (TempPictureUpdateRequest pictureUpdateRequest : request.getPictures()) {

            PlaceUpdateRequest placeUpdateRequest = pictureUpdateRequest.getPlace();

            //request??? id??? ?????? Picture??? ????????? ?????? Picture ????????? ??????
            if (ObjectUtils.isEmpty(pictureUpdateRequest.getId())) {

                TempPictureCreateRequest pictureCreateRequest = new TempPictureCreateRequest(pictureUpdateRequest);

                foundAlbum.addPicture(Picture.create(
                        pictureCreateRequest.isThumbnail(),
                        pictureCreateRequest.getOrder(),
                        pictureCreateRequest.getImageUrl(),
                        pictureCreateRequest.getDescription(),
                        pictureCreateRequest.getPlace().toEntity())
                );
            }
            //????????? ?????? Picture ??? Place ????????????
            else {

                Picture foundPicture = getPictureThrowable(pictureUpdateRequest.getId());

                getPlaceThrowable(foundPicture.getPlace().getId()).update(placeUpdateRequest.toEntity());

                foundPicture.update(pictureUpdateRequest.toEntity());
            }

            //Album ????????????
            foundAlbum.update(request.toEntity());
        }
    }

    @Transactional
    public List<String> delete(Long albumId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Album foundAlbum = getAlbumThrowable(albumId);

        //????????? ????????? ????????? ??????
        checkUserOwnAlbum(user, foundAlbum);

        albumRepository.delete(foundAlbum);

        return foundAlbum.getPictures().stream().map(picture -> CUrlUtils.extractFileNameFrom(picture.getImageUrl())).collect(Collectors.toList());
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

    private Place getPlaceThrowable(Long placeId) {
        return placeRepository.findById(placeId).orElseThrow(CPlaceNotFoundException::new);
    }

    private Album getAlbumThrowable(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(CAlbumNotFoundException::new);
    }

    private Picture getPictureThrowable(Long pictureId) {
        return pictureRepository.findById(pictureId)
                .orElseThrow(CPictureNotFoundException::new);
    }

    private User getUserThrowable(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(CUserNotFoundException::new);
    }
}
