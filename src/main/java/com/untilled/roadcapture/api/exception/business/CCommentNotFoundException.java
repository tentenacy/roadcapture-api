package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException;

public class CCommentNotFoundException extends CEntityNotFoundException {
    public CCommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}