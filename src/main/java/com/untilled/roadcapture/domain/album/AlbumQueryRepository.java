package com.untilled.roadcapture.domain.album;

import com.untilled.roadcapture.api.dto.album.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AlbumQueryRepository {
    Page<AlbumsResponse> searchAlbums(AlbumsCondition cond, Pageable pageable, Long userId);
    Optional<AlbumResponse> getAlbum(Long albumId);
    Page<UserAlbumsResponse> searchUserAlbums(UserAlbumsCondition cond, Pageable pageable, Long userId);
}
