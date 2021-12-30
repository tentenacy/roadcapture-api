package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CCommentNotFoundException extends CEntityNotFoundException {
    public CCommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
