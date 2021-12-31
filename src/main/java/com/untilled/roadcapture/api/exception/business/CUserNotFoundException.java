package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException;

public class CUserNotFoundException extends CEntityNotFoundException {
    public CUserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
