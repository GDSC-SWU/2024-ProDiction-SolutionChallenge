package com.pro_diction.server.global.handler;

import com.pro_diction.server.global.common.ApiResponseDto;
import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponseDto> handleException(GeneralException e) {
        int code = e.getErrorCode().getHttpStatus().value();
        String message = e.getMessage();

        return ResponseEntity.status(code).body(ApiResponseDto.of(code, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleException(Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        int code = errorCode.getHttpStatus().value();

        e.printStackTrace();

        return ResponseEntity.status(code).body(ApiResponseDto.of(code, e.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponseDto> exceptionHandler(HttpRequestMethodNotSupportedException e) {
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        int code = errorCode.getHttpStatus().value();
        String message = errorCode.getMessage();

        return ResponseEntity.status(code).body(ApiResponseDto.of(code, message));
    }
}
