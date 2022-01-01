package com.untilled.roadcapture.api.exception.social;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.security.CSecurityException;

public class CSocialTokenValidFailedException extends CSocialException {

    public CSocialTokenValidFailedException() {
        super(ErrorCode.SOCIAL_TOKEN_VALID_FAILED);
    }
}
