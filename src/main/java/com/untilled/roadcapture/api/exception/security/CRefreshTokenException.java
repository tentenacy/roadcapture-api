package com.untilled.roadcapture.api.exception.security;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.security.CTokenException;

public class CRefreshTokenException extends CTokenException {

    public CRefreshTokenException() {
        super(ErrorCode.REFRESH_TOKEN_ERROR);
    }
}
