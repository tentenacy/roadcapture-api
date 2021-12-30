package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CEmailDuplicatedException extends CInvalidValueException {
    public CEmailDuplicatedException() {
        super(ErrorCode.EMAIL_DUPLICATION);
    }
}
