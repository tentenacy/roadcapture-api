package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CPictureNotFoundException extends CEntityNotFoundException {
    public CPictureNotFoundException() {
        super(ErrorCode.PICTURE_NOT_FOUND);
    }
}
