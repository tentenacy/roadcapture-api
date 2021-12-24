package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.base.ErrorCode;

public class AlbumNotFoundException extends EntityNotFoundException {
    public AlbumNotFoundException() {
        super(ErrorCode.ALBUM_NOT_FOUND);
    }
}
