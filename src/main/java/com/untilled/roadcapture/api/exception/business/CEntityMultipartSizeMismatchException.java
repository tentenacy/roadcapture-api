package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CEntityMultipartSizeMismatchException extends CInvalidValueException {

    public CEntityMultipartSizeMismatchException() {
        super(ErrorCode.ENTITY_MULTIPART_SIZE_MISMATCH);
    }
}
