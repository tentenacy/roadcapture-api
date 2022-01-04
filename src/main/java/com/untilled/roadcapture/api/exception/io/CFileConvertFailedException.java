package com.untilled.roadcapture.api.exception.io;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CFileConvertFailedException extends CIOException {

    public CFileConvertFailedException() {
        super(ErrorCode.FILE_CONVERT_FAILED);
    }
}
