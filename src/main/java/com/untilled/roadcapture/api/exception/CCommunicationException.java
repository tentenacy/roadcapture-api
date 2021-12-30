package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import lombok.Getter;

public class CCommunicationException extends CSocialException {

    public CCommunicationException() {
        super(ErrorCode.COMMUNICATION_ERROR);
    }
}
