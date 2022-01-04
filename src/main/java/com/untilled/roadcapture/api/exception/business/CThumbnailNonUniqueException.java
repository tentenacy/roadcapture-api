package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CThumbnailNonUniqueException extends CInvalidValueException {

    public CThumbnailNonUniqueException() {
        super(ErrorCode.THUMBNAIL_NON_UNIQUE);
    }
}
