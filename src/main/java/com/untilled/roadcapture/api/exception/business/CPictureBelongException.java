package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CPictureBelongException extends CEntityBelongException {

    public CPictureBelongException() {
        super(ErrorCode.PICTURE_BELONG_ERROR);
    }
}
