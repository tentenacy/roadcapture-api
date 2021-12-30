package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class TokenException extends CSecurityException {
    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
