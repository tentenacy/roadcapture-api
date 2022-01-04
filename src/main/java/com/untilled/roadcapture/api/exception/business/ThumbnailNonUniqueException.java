package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class ThumbnailNonUniqueException extends CInvalidValueException {

    public ThumbnailNonUniqueException() {
        super(ErrorCode.THUMBNAIL_NON_UNIQUE);
    }
}
