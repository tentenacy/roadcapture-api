package com.untilled.roadcapture.api.exception.business;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CUserToFollowNotFoundException extends CEntityNotFoundException {
    public CUserToFollowNotFoundException() {
        super(ErrorCode.USERTOFOLLOW_NOT_FOUND);
    }
}
