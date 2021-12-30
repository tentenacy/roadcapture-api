package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CUserNotFoundException extends CEntityNotFoundException {
    public CUserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
