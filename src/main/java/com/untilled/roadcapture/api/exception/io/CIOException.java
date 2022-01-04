package com.untilled.roadcapture.api.exception.io;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import lombok.Data;
import lombok.Getter;

@Getter
public class CIOException extends RuntimeException {

    private ErrorCode errorCode;

    public CIOException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
