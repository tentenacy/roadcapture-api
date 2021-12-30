package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CEmailLoginFailedException extends CInvalidValueException {
    public CEmailLoginFailedException() {
        super(ErrorCode.EMAIL_LOGIN_FAIL);
    }
}
