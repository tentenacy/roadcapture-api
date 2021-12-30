package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import lombok.Getter;

@Getter
public class CBusinessException extends RuntimeException {

    private ErrorCode errorCode;

    public CBusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
