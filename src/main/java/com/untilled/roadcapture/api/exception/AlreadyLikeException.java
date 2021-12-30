package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class AlreadyLikeException extends InvalidValueException {
    public AlreadyLikeException() {
        super(ErrorCode.ALREADY_LIKE);
    }
}
