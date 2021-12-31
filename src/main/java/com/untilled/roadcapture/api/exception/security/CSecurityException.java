package com.untilled.roadcapture.api.exception.security;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import lombok.Getter;

@Getter
public class CSecurityException extends RuntimeException {

    private ErrorCode errorCode;

    public CSecurityException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
