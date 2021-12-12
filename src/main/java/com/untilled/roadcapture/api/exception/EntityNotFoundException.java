package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.base.ErrorCode;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
