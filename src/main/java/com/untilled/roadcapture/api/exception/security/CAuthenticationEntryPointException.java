package com.untilled.roadcapture.api.exception.security;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.security.CSecurityException;

public class CAuthenticationEntryPointException extends CSecurityException {

    public CAuthenticationEntryPointException() {
        super(ErrorCode.ACCESS_TOKEN_ERROR);
    }
}
