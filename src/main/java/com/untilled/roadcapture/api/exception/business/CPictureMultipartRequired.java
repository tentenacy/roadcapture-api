package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CPictureMultipartRequired extends CInvalidValueException {

    public CPictureMultipartRequired() {
        super(ErrorCode.PICTURE_MULTIPART_REQUIRED);
    }
}
