package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class EmailDuplicatedException extends InvalidValueException {
    public EmailDuplicatedException() {
        super(ErrorCode.EMAIL_DUPLICATION);
    }
}
