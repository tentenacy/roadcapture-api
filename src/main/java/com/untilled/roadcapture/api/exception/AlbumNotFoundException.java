package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.base.ErrorCode;

public class AlbumNotFoundException extends EntityNotFoundException {
    public AlbumNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
