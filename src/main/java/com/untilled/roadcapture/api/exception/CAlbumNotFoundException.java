package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CAlbumNotFoundException extends CEntityNotFoundException {
    public CAlbumNotFoundException() {
        super(ErrorCode.ALBUM_NOT_FOUND);
    }
}
