package com.untilled.roadcapture.api.exception.social;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CInvalidSocialTypeException extends CSocialException{
    public CInvalidSocialTypeException() {
        super(ErrorCode.INVALID_SOCIAL_TYPE);
    }
}
