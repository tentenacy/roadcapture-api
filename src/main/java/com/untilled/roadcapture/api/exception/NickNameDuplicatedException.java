package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.base.ErrorCode;

public class NickNameDuplicatedException extends InvalidValueException {
    public NickNameDuplicatedException() {
        super(ErrorCode.NICKNAME_EMAIL_DUPLICATION);
    }
}
