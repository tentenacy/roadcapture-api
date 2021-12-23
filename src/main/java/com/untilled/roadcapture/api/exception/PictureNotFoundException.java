package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.base.ErrorCode;

public class PictureNotFoundException extends EntityNotFoundException {
    public PictureNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
