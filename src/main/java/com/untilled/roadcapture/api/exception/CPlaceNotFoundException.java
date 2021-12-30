package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CPlaceNotFoundException extends CEntityNotFoundException {
    public CPlaceNotFoundException() {
        super(ErrorCode.PLACE_NOT_FOUND);
    }
}
