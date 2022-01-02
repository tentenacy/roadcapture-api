package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CEntityBelongException extends CBusinessException {

    public CEntityBelongException(ErrorCode errorCode) {
        super(errorCode);
    }
}
