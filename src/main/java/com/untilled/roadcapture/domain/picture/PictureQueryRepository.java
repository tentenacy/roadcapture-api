package com.untilled.roadcapture.domain.picture;

import java.util.List;
import java.util.Optional;

public interface PictureQueryRepository {
    Optional<List<Picture>> getPicturesByAlbumId(Long albumId);
}
