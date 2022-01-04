package com.untilled.roadcapture.api.exception.social;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CSocialCommunicationException extends CSocialException {

    public CSocialCommunicationException() {
        super(ErrorCode.SOCIAL_COMMUNICATION_ERROR);
    }
}
