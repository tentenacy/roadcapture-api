package com.untilled.roadcapture.api.exception.social;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import lombok.Getter;

@Getter
public class CSocialException extends RuntimeException {

    private ErrorCode errorCode;

    public CSocialException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public static class CInvalidSocialTypeException extends CSocialException{
        public CInvalidSocialTypeException() {
            super(ErrorCode.INVALID_SOCIAL_TYPE);
        }
    }

    public static class CSocialAgreementException  extends CSocialException {

        public CSocialAgreementException() {
            super(ErrorCode.SOCIAL_AGREEMENT_ERROR);
        }
    }

    public static class CSocialCommunicationException extends CSocialException {

        public CSocialCommunicationException() {
            super(ErrorCode.SOCIAL_COMMUNICATION_ERROR);
        }
    }

    public static class CSocialTokenValidFailedException extends CSocialException {

        public CSocialTokenValidFailedException() {
            super(ErrorCode.SOCIAL_TOKEN_VALID_FAILED);
        }
    }
}
