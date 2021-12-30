package com.untilled.roadcapture.api.dto.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // COMMON
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "CMM-001", "잘못된 입력입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "CMM-002", "Method Not Allowed"),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "CMM-003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "CMM-004", "Server Error"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST.value(), "CMM-005", "Invalid Type Value"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "CMM-006", "접근이 거부되었습니다."),
    JSON_WRITE_ERROR(HttpStatus.UNAUTHORIZED.value(), "CMM-007", "JSON content that are not pure I/O problems"),
    COMMUNICATION_ERROR(HttpStatus.BAD_REQUEST.value(), "CMM-008", "소셜 인증 과정 중 오류가 발생했습니다."),

    //TOKEN
    ACCESS_TOKEN_ERROR(HttpStatus.UNAUTHORIZED.value(), "TKN-001", "액세스 토큰이 만료되거나 잘못된 값입니다."),
    REFRESH_TOKEN_ERROR(HttpStatus.UNAUTHORIZED.value(), "TKN-002", "리프레시 토큰이 만료되거나 잘못된 값입니다."),
    TOKEN_PARSE_ERROR(HttpStatus.UNAUTHORIZED.value(), "TKN-003", "해석할 수 없는 토큰입니다."),

    //USER
    EMAIL_LOGIN_FAIL(HttpStatus.BAD_REQUEST.value(), "USR-001", "존재하지 않는 계정이거나, 잘못된 비밀번호입니다."),
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST.value(), "USR-002", "중복된 이메일의 계정이 존재합니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "USR-003", "사용자가 존재하지 않습니다."),
    USER_NOT_AUTHENTICATION(HttpStatus.UNAUTHORIZED.value(), "USR-004", "인증된 사용자가 아닙니다."),
    NICKNAME_DUPLICATION(HttpStatus.BAD_REQUEST.value(), "USR-005", "중복된 닉네임의 계정이 존재합니다."),

    //ALBUM
    ALBUM_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "ABM-001", "앨범이 존재하지 않습니다."),

    //PICTURE
    PICTURE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "PTR-001", "사진이 존재하지 않습니다."),

    //PLACE
    PLACE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "PLC-001", "장소가 존재하지 않습니다."),

    //COMMENT
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "CMT-001", "댓글이 존재하지 않습니다."),

    //LIKE
    ALREADY_LIKE(HttpStatus.BAD_REQUEST.value(), "LIK-001", "이미 좋아요를 했습니다."),
    LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "LIK-002", "좋아요가 존재하지 않습니다."),
            ;

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}