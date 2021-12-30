package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CLikeNotFoundException extends CEntityNotFoundException {
    public CLikeNotFoundException() {
        super(ErrorCode.LIKE_NOT_FOUND);
    }
}
