package com.untilled.roadcapture.api.exception.io;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CInvalidFileFormatException extends CIOException {

    public CInvalidFileFormatException() {
        super(ErrorCode.INVALID_FILE_FORMAT);
    }
}
