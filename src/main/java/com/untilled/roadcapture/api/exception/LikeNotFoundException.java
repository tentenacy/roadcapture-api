package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.base.ErrorCode;

public class LikeNotFoundException extends EntityNotFoundException {
    public LikeNotFoundException() {
        super(ErrorCode.LIKE_NOT_FOUND);
    }
}
