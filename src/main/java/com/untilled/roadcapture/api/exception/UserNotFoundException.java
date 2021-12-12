package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.base.ErrorCode;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
