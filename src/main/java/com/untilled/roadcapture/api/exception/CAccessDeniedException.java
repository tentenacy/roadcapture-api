package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import lombok.Getter;

@Getter
public class CAccessDeniedException extends RuntimeException {

    private ErrorCode errorCode;

    public CAccessDeniedException() {
        this.errorCode = ErrorCode.HANDLE_ACCESS_DENIED;
    }
}
