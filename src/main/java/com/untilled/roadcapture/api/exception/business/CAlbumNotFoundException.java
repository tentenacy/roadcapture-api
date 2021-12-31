package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException;

public class CAlbumNotFoundException extends CEntityNotFoundException {
    public CAlbumNotFoundException() {
        super(ErrorCode.ALBUM_NOT_FOUND);
    }
}
