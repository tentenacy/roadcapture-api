package com.untilled.roadcapture.api.exception;

import com.untilled.roadcapture.api.dto.common.ErrorCode;

public class CommentNotFoundException extends EntityNotFoundException {
    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
