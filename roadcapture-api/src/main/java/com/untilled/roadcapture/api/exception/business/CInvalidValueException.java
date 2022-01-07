package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CInvalidValueException extends CBusinessException {
    public CInvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class CAlreadyLikeException extends CInvalidValueException {
        public CAlreadyLikeException() {
            super(ErrorCode.ALREADY_LIKE);
        }
    }

    public static class CAlreadySignedupException extends CInvalidValueException {
        public CAlreadySignedupException() {
            super(ErrorCode.ALREADY_SIGNEDUP);
        }
    }

    public static class CEmailLoginFailedException extends CInvalidValueException {
        public CEmailLoginFailedException() {
            super(ErrorCode.EMAIL_LOGIN_FAIL);
        }
    }

    public static class CMultiPartKeyMismatchException extends CInvalidValueException {

        public CMultiPartKeyMismatchException() {
            super(ErrorCode.MULTIPART_KEY_MISMATCH);
        }
    }

    public static class CPictureMultipartRequired extends CInvalidValueException {

        public CPictureMultipartRequired() {
            super(ErrorCode.PICTURE_MULTIPART_REQUIRED);
        }
    }

    public static class CThumbnailNonUniqueException extends CInvalidValueException {

        public CThumbnailNonUniqueException() {
            super(ErrorCode.THUMBNAIL_NON_UNIQUE);
        }
    }

    public static class CFollowMyselfException extends CInvalidValueException {

        public CFollowMyselfException() {
            super(ErrorCode.FOLLOW_MYSELF_ERROR);
        }
    }

    public static class CUnfollowMyselfException extends CInvalidValueException {

        public CUnfollowMyselfException() {
            super(ErrorCode.UNFOLLOW_MYSELF_ERROR);
        }
    }


}
