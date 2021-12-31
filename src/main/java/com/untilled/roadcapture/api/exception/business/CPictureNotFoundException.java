package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException;

public class CPictureNotFoundException extends CEntityNotFoundException {
    public CPictureNotFoundException() {
        super(ErrorCode.PICTURE_NOT_FOUND);
    }
}
