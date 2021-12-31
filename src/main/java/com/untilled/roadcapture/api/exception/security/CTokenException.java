package com.untilled.roadcapture.api.exception.security;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.security.CSecurityException;

public class CTokenException extends CSecurityException {
    public CTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
