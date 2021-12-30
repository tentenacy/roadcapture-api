package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class InvalidValueException extends BusinessException {
    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
