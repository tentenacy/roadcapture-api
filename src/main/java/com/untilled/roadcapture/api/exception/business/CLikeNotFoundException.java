package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException;

public class CLikeNotFoundException extends CEntityNotFoundException {
    public CLikeNotFoundException() {
        super(ErrorCode.LIKE_NOT_FOUND);
    }
}
