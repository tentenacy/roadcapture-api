package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CAlreadyLikeException extends CInvalidValueException {
    public CAlreadyLikeException() {
        super(ErrorCode.ALREADY_LIKE);
    }
}
