package com.untilled.roadcapture.api.exception.social;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import lombok.Getter;

@Getter
public class CSocialException extends RuntimeException {

    private ErrorCode errorCode;

    public CSocialException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
