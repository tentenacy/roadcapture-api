package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import lombok.Getter;

public class CAuthenticationEntryPointException extends CSecurityException {

    public CAuthenticationEntryPointException() {
        super(ErrorCode.ACCESS_TOKEN_ERROR);
    }
}
