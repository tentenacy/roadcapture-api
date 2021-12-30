package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import lombok.Getter;

public class CAccessDeniedException extends TokenException {

    public CAccessDeniedException() {
        super(ErrorCode.HANDLE_ACCESS_DENIED);
    }
}
