package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import lombok.Getter;

public class AuthenticationEntryPointException extends CSecurityException {

    public AuthenticationEntryPointException() {
        super(ErrorCode.ACCESS_TOKEN_ERROR);
    }
}
