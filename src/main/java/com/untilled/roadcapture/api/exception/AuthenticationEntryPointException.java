package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import lombok.Getter;

@Getter
public class AuthenticationEntryPointException extends RuntimeException {

    private ErrorCode errorCode;

    public AuthenticationEntryPointException() {
        this.errorCode = ErrorCode.JWT_ERROR;
    }
}
