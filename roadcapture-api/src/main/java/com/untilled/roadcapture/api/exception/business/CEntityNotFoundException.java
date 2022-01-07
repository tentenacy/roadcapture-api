package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CEntityNotFoundException extends CBusinessException {
    public CEntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class CAlbumNotFoundException extends CEntityNotFoundException {
        public CAlbumNotFoundException() {
            super(ErrorCode.ALBUM_NOT_FOUND);
        }
    }

    public static class CCommentNotFoundException extends CEntityNotFoundException {
        public CCommentNotFoundException() {
            super(ErrorCode.COMMENT_NOT_FOUND);
        }
    }

    public static class CLikeNotFoundException extends CEntityNotFoundException {
        public CLikeNotFoundException() {
            super(ErrorCode.LIKE_NOT_FOUND);
        }
    }

    public static class CPictureNotFoundException extends CEntityNotFoundException {
        public CPictureNotFoundException() {
            super(ErrorCode.PICTURE_NOT_FOUND);
        }
    }

    public static class CPlaceNotFoundException extends CEntityNotFoundException {
        public CPlaceNotFoundException() {
            super(ErrorCode.PLACE_NOT_FOUND);
        }
    }

    public static class CUserNotFoundException extends CEntityNotFoundException {
        public CUserNotFoundException() {
            super(ErrorCode.USER_NOT_FOUND);
        }
    }

    public static class CUserToFollowNotFoundException extends CEntityNotFoundException {
        public CUserToFollowNotFoundException() {
            super(ErrorCode.USERTOFOLLOW_NOT_FOUND);
        }
    }

    public static class CFollowerNotFoundException extends CEntityNotFoundException {
        public CFollowerNotFoundException() {
            super(ErrorCode.FOLLOWER_NOT_FOUND);
        }
    }

}
