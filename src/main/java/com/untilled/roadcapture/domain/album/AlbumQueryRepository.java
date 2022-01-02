package com.untilled.roadcapture.domain.album;

import com.untilled.roadcapture.api.dto.album.AlbumResponse;
import com.untilled.roadcapture.api.dto.album.AlbumsCondition;
import com.untilled.roadcapture.api.dto.album.AlbumsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AlbumQueryRepository {
    Page<AlbumsResponse> search(AlbumsCondition cond, Pageable pageable);
    Optional<AlbumResponse> getAlbum(Long albumId);
}
