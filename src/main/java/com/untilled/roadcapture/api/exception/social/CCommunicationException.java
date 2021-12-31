package com.untilled.roadcapture.api.exception.social;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CCommunicationException extends CSocialException {

    public CCommunicationException() {
        super(ErrorCode.COMMUNICATION_ERROR);
    }
}
