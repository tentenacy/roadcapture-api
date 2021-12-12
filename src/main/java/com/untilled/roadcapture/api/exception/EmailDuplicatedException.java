package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.base.ErrorCode;

public class EmailDuplicatedException extends InvalidValueException {
    public EmailDuplicatedException() {
        super(ErrorCode.EMAIL_DUPLICATION);
    }
}
