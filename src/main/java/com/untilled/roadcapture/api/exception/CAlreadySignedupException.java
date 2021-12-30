package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CAlreadySignedupException extends CInvalidValueException{
    public CAlreadySignedupException() {
        super(ErrorCode.ALREADY_SIGNEDUP);
    }
}
