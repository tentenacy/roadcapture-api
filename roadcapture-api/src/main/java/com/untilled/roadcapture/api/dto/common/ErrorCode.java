package com.untilled.roadcapture.api.dto.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /**
     * COMMON
     */
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "CMM-001", "잘못된 입력입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "CMM-002", "Method Not Allowed"),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "CMM-003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "CMM-004", "Server Error"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST.value(), "CMM-005", "Invalid Type Value"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "CMM-006", "접근이 거부되었습니다."),
    JSON_WRITE_ERROR(HttpStatus.UNAUTHORIZED.value(), "CMM-007", "JSON content that are not pure I/O problems"),

    /**
     * IO
     */
    FILE_CONVERT_FAILED(HttpStatus.BAD_REQUEST.value(), "IO-001", "파일을 변환할 수 없습니다."),
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST.value(), "IO-002", "잘못된 형식의 파일입니다."),
    CLOUD_COMMUNICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "IO-003", "파일 업로드 중 오류가 발생했습니다."),

    /**
     * SOCIAL
     */
    SOCIAL_COMMUNICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "SCL-001", "소셜 인증 과정 중 오류가 발생했습니다."),
    SOCIAL_AGREEMENT_ERROR(HttpStatus.BAD_REQUEST.value(), "SCL-002", "필수동의 항목에 대해 동의가 필요합니다."),
    INVALID_SOCIAL_TYPE(HttpStatus.BAD_REQUEST.value(), "SCL-003", "알 수 없는 소셜 타입입니다."),
    SOCIAL_TOKEN_VALID_FAILED(HttpStatus.UNAUTHORIZED.value(), "SCR-004", "소셜 액세스 토큰 검증에 실패했습니다."),

    /**
     * SECURITY
     */
    ACCESS_TOKEN_ERROR(HttpStatus.UNAUTHORIZED.value(), "SCR-001", "액세스 토큰이 만료되거나 잘못된 값입니다."),
    REFRESH_TOKEN_ERROR(HttpStatus.UNAUTHORIZED.value(), "SCR-002", "리프레시 토큰이 만료되거나 잘못된 값입니다."),
    TOKEN_PARSE_ERROR(HttpStatus.UNAUTHORIZED.value(), "SCR-003", "해석할 수 없는 토큰입니다."),

    /**
     * BUSINESS
     */
    EMAIL_LOGIN_FAIL(HttpStatus.BAD_REQUEST.value(), "BIZ-001", "존재하지 않는 계정이거나, 잘못된 비밀번호입니다."),
    ALREADY_SIGNEDUP(HttpStatus.BAD_REQUEST.value(), "BIZ-002", "이미 가입한 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "BIZ-003", "사용자가 존재하지 않습니다."),
    USER_NOT_AUTHENTICATION(HttpStatus.UNAUTHORIZED.value(), "BIZ-004", "인증된 사용자가 아닙니다."),
    ALBUM_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "BIZ-005", "앨범이 존재하지 않습니다."),
    PICTURE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "BIZ-006", "사진이 존재하지 않습니다."),
    PLACE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "BIZ-007", "장소가 존재하지 않습니다."),
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "BIZ-008", "댓글이 존재하지 않습니다."),
    ALREADY_LIKE(HttpStatus.BAD_REQUEST.value(), "BIZ-009", "이미 좋아요를 했습니다."),
    LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "BIZ-010", "좋아요가 존재하지 않습니다."),
    USER_OWN_ALBUM_ERROR(HttpStatus.BAD_REQUEST.value(), "BIZ-011", "사용자의 앨범이 아닙니다."),
    PICTURE_BELONG_ERROR(HttpStatus.BAD_REQUEST.value(), "BIZ-012", "앨범의 사진이 아닙니다."),
    THUMBNAIL_NON_UNIQUE(HttpStatus.BAD_REQUEST.value(), "BIZ-013", "앨범 썸네일이 유일하지 않습니다."),
    MULTIPART_KEY_MISMATCH(HttpStatus.BAD_REQUEST.value(), "BIZ-014", "업로드 파일 키와 일치하는 데이터가 없습니다."),
    PICTURE_MULTIPART_REQUIRED(HttpStatus.BAD_REQUEST.value(), "BIZ-015", "사진을 생성하기 위한 파일이 필요합니다."),
    USERTOFOLLOW_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "BIZ-016", "팔로우할 사용자가 존재하지 않습니다."),
    ALREADY_FOLLOW(HttpStatus.BAD_REQUEST.value(), "BIZ-017", "이미 팔로우를 했습니다."),
    FOLLOW_MYSELF_ERROR(HttpStatus.BAD_REQUEST.value(), "BIZ-018", "자신은 팔로우할 수 없습니다."),
    UNFOLLOW_MYSELF_ERROR(HttpStatus.BAD_REQUEST.value(), "BIZ-019", "자신은 언팔로우할 수 없습니다."),
    FOLLOWER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "BIZ-020", "사용자를 팔로우하지 않았습니다."),
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