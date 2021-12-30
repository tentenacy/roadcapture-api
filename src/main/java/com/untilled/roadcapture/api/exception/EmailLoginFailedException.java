package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class EmailLoginFailedException extends InvalidValueException {
    public EmailLoginFailedException() {
        super(ErrorCode.EMAIL_LOGIN_FAIL);
    }
}
