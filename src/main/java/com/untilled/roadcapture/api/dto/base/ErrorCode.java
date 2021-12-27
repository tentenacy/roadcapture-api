package com.untilled.roadcapture.api.dto.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // COMMON
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "CMM-001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "CMM-002", "Method Not Allowed"),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "CMM-003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "CMM-004", "Server Error"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST.value(), "CMM-005", "Invalid Type Value"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "CMM-006", "Access is Denied"),
    JSON_WRITE_ERROR(HttpStatus.UNAUTHORIZED.value(), "CMM-007", "JSON content that are not pure I/O problems"),

    //JWT
    JWT_ERROR(HttpStatus.UNAUTHORIZED.value(), "JWT-001", "토큰이 없거나 잘못된 값입니다."),
    JWT_PARSE_ERROR(HttpStatus.UNAUTHORIZED.value(), "JWT-002", "해석할 수 없는 토큰입니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "JWT-003", "토큰이 만료되었습니다."),

    //USER
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "USR-001", "이메일이 존재하지 않습니다."),
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST.value(), "USR-002", "중복된 이메일의 계정이 존재합니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "USR-003", "사용자가 존재하지 않습니다."),
    USER_NOT_AUTHENTICATION(HttpStatus.UNAUTHORIZED.value(), "USR-004", "인증된 사용자가 아닙니다."),
    NICKNAME_EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST.value(), "USR-005", "중복된 닉네임의 계정이 존재합니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST.value(), "USR-006", "비밀번호가 일치하지 않습니다."),

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