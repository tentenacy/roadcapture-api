package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException;

public class CPlaceNotFoundException extends CEntityNotFoundException {
    public CPlaceNotFoundException() {
        super(ErrorCode.PLACE_NOT_FOUND);
    }
}
