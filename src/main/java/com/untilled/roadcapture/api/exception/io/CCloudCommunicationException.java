package com.untilled.roadcapture.api.exception.io;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CCloudCommunicationException extends CIOException {

    public CCloudCommunicationException() {
        super(ErrorCode.CLOUD_COMMUNICATION_ERROR);
    }
}
