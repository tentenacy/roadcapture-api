package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CUsernameDuplicatedException extends CInvalidValueException {
    public CUsernameDuplicatedException() {
        super(ErrorCode.NICKNAME_DUPLICATION);
    }
}
