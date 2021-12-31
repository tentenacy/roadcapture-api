package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.business.CInvalidValueException;

public class CAlreadyLikeException extends CInvalidValueException {
    public CAlreadyLikeException() {
        super(ErrorCode.ALREADY_LIKE);
    }
}
