package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CMultiPartKeyMismatchException extends CInvalidValueException {

    public CMultiPartKeyMismatchException() {
        super(ErrorCode.MULTIPART_KEY_MISMATCH);
    }
}
