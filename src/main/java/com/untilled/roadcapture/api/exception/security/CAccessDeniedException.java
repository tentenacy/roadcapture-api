package com.untilled.roadcapture.api.exception.security;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.security.CTokenException;

public class CAccessDeniedException extends CTokenException {

    public CAccessDeniedException() {
        super(ErrorCode.HANDLE_ACCESS_DENIED);
    }
}
