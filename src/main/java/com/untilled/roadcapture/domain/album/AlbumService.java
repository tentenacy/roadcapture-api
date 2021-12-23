package com.untilled.roadcapture.domain.album;

import com.untilled.roadcapture.api.dto.album.*;
import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureUpdateRequest;
import com.untilled.roadcapture.api.exception.AlbumNotFoundException;
import com.untilled.roadcapture.api.exception.PictureNotFoundException;
import com.untilled.roadcapture.api.exception.UserNotFoundException;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.picture.PictureRepository;
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

    public Page<AlbumsResponse> getAlbums(AlbumSearchCondition cond, Pageable pageable) {
        return albumRepository.search(cond, pageable);
    }

    public AlbumResponse getAlbum(Long albumId) {
        return new AlbumResponse(albumRepository.get(albumId).orElseThrow(AlbumNotFoundException::new));
    }

    @Transactional
    public Album create(AlbumCreateRequest request) {

        List<Picture> pictures = request.getPictures().stream()
                .map(pictureCreateRequest -> pictureCreateRequest.toEntity())
                .collect(Collectors.toList());

        return albumRepository.save(Album.create(
                request.getTitle(),
                request.getDescription(),
                request.getThumbnailUrl(),
                pictures,
                getUserIfExists(request.getUserId())
        ));
    }

    @Transactional
    public void update(Long albumId, AlbumUpdateRequest request) {
        getAlbumIfExists(albumId)
                .update(
                        request.getTitle(),
                        request.getDescription(),
                        request.getThumbnailUrl()
                );
    }

    @Transactional
    public void delete(Long albumId) {
        albumRepository.delete(getAlbumIfExists(albumId));
    }

    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private Album getAlbumIfExists(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(AlbumNotFoundException::new);
    }
}
