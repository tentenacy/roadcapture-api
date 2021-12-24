package com.untilled.roadcapture.domain.album;

import com.untilled.roadcapture.api.dto.album.AlbumResponse;
import com.untilled.roadcapture.api.dto.album.AlbumSearchCondition;
import com.untilled.roadcapture.api.dto.album.AlbumsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AlbumQueryRepository {
    Page<AlbumsResponse> search(AlbumSearchCondition cond, Pageable pageable);
    Optional<Album> getFetchJoin(Long albumId);
}
