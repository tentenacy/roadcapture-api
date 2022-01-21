package com.untilled.roadcapture.domain.album;

import com.untilled.roadcapture.api.dto.album.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AlbumQueryRepository {
    Page<AlbumsResponse> getAlbums(AlbumsCondition cond, Pageable pageable, Long userId);
    Page<AlbumsResponse> getFollowingAlbums(FollowingAlbumsCondition cond, Pageable pageable, Long userId);
    Optional<AlbumResponse> getAlbum(Long albumId, Long userId);
    Page<StudioAlbumsResponse> getStudioAlbums(MyStudioAlbumsCondition cond, Pageable pageable, Long userId);
}
