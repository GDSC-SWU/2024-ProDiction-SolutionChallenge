package com.pro_diction.server.global.constant;

import com.pro_diction.server.global.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    OK(0, HttpStatus.OK, "Ok"),

    BAD_REQUEST(10000, HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_ERROR(10001, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(10002, HttpStatus.NOT_FOUND, "Requested resource is not found"),
    METHOD_NOT_ALLOWED(10003, HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed."),

    INTERNAL_SERVER_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    DATA_ACCESS_ERROR(20001, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),

    AUTHENTICATION_FAILED(40000, HttpStatus.UNAUTHORIZED, "User unauthorized"),
    AUTHORIZATION_FAILED(40001, HttpStatus.UNAUTHORIZED, "Authorization failed."),
    ACCESS_TOKEN_REQUIRED(40002, HttpStatus.UNAUTHORIZED, "Access Token required."),
    REFRESH_TOKEN_REQUIRED(40003, HttpStatus.UNAUTHORIZED, "Refresh Token required."),
    INVALID_TOKEN(40004, HttpStatus.UNAUTHORIZED, "Invalid token."),
    EXPIRED_JWT(40005, HttpStatus.UNAUTHORIZED, "Token expired."),
    LOGIN_REQUIRED(40006, HttpStatus.FORBIDDEN, "Login required."),
    UNAUTHORIZED_ROLE(40007, HttpStatus.FORBIDDEN, "Unauthorized role."),

    // Google Login Error
    ID_TOKEN_REQUIRED(11000, HttpStatus.BAD_REQUEST, "Id token required."),
    LOGIN_FAILED(11001, HttpStatus.UNAUTHORIZED, "Login failed."),
    INVALID_ID_TOKEN(11002, HttpStatus.UNAUTHORIZED, "Invalid Code."),

    // Member Error
    MEMBER_NOT_FOUND(12000, HttpStatus.BAD_REQUEST, "Member not found."),

    // Study Error
    STUDY_NOT_FOUND(13000, HttpStatus.BAD_REQUEST, "Study not found."),
    CATEGORY_NOT_FOUND(13001, HttpStatus.BAD_REQUEST, "Category not found."),
    SUBCATEGORY_NOT_FOUND(13002, HttpStatus.BAD_REQUEST, "Subcategory not found."),
    ONLY_ONE_PARAMETER_ALLOWED(13003, HttpStatus.BAD_REQUEST, "Only one of subCategoryId and parentStudyId should be provided."),
    INVALID_FINAL_CONSONANT_PARENT_STUDY(13004, HttpStatus.BAD_REQUEST, "This study is not final consonant parent study."),
    CATEGORY_HAS_NOT_FINAL_CONSONANT(13005, HttpStatus.BAD_REQUEST, "This category has not final consonant."),

    // Test Error
    INVALID_STAGE_NUM(14000, HttpStatus.BAD_REQUEST, "Invalid stage number.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
        // 결과 예시 - "Validation error - Reason why it isn't valid"
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    public static ErrorCode valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new GeneralException("HttpStatus is null.");
        }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return ErrorCode.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return ErrorCode.INTERNAL_SERVER_ERROR;
                    } else {
                        return ErrorCode.OK;
                    }
                });
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }
}
