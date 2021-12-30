package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CAccessDeniedException extends CTokenException {

    public CAccessDeniedException() {
        super(ErrorCode.HANDLE_ACCESS_DENIED);
    }
}
