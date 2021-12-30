package com.untilled.roadcapture.api.controller.advice;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.dto.common.ErrorResponse;
import com.untilled.roadcapture.api.exception.AuthenticationEntryPointException;
import com.untilled.roadcapture.api.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * @Validated 로 검증 시 binding 못하는 경우 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 타입이 일치하지 않아 binding 못하는 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException() {
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE), HttpStatus.BAD_REQUEST);
    }

    /**
     * Jwt가 잘못된 경우
     */
    @ExceptionHandler(AuthenticationEntryPointException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationEntryPointException(final AuthenticationEntryPointException e) {
        return new ResponseEntity<>(ErrorResponse.of(e.getErrorCode()), HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    /**
     * BusinessException 하위 클래스
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        return new ResponseEntity<>(ErrorResponse.of(e.getErrorCode()), HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

}
