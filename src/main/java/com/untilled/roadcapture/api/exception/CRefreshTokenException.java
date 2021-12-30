package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CRefreshTokenException extends CTokenException {

    public CRefreshTokenException() {
        super(ErrorCode.REFRESH_TOKEN_ERROR);
    }
}
