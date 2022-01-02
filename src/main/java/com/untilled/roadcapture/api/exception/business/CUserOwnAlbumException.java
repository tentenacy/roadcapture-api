package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CUserOwnAlbumException extends CEntityBelongException {

    public CUserOwnAlbumException() {
        super(ErrorCode.USER_OWN_ALBUM_ERROR);
    }
}
