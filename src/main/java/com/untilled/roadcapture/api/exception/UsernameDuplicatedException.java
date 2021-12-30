package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class UsernameDuplicatedException extends InvalidValueException {
    public UsernameDuplicatedException() {
        super(ErrorCode.NICKNAME_DUPLICATION);
    }
}
