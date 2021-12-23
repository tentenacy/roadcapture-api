package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.base.ErrorCode;

public class PlaceNotFoundException extends EntityNotFoundException {
    public PlaceNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
