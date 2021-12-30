package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class PictureNotFoundException extends EntityNotFoundException {
    public PictureNotFoundException() {
        super(ErrorCode.PICTURE_NOT_FOUND);
    }
}
