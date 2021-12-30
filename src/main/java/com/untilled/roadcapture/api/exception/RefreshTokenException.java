package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class RefreshTokenException extends TokenException {

    public RefreshTokenException() {
        super(ErrorCode.REFRESH_TOKEN_ERROR);
    }
}
