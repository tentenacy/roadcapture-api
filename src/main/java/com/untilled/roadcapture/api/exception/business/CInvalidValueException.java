package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CInvalidValueException extends CBusinessException {
    public CInvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
