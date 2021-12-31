package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.business.CInvalidValueException;

public class CAlreadySignedupException extends CInvalidValueException {
    public CAlreadySignedupException() {
        super(ErrorCode.ALREADY_SIGNEDUP);
    }
}
