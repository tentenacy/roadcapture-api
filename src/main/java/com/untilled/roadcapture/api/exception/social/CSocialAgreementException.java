package com.untilled.roadcapture.api.exception.social;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CSocialAgreementException  extends CSocialException {

    public CSocialAgreementException() {
        super(ErrorCode.SOCIAL_AGREEMENT_ERROR);
    }
}