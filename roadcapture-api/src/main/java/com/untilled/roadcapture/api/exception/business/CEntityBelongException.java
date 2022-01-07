package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CEntityBelongException extends CBusinessException {

    public CEntityBelongException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class CPictureBelongException extends CEntityBelongException {

        public CPictureBelongException() {
            super(ErrorCode.PICTURE_BELONG_ERROR);
        }
    }

    public static class CUserOwnAlbumException extends CEntityBelongException {

        public CUserOwnAlbumException() {
            super(ErrorCode.USER_OWN_ALBUM_ERROR);
        }
    }

}
