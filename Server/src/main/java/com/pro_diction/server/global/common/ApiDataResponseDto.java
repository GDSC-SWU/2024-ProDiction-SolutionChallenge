package com.pro_diction.server.global.common;

import com.pro_diction.server.global.constant.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiDataResponseDto<T> extends ApiResponseDto {
    private final T data;

    private ApiDataResponseDto(T data) {
        super(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
        this.data = data;
    }

    private ApiDataResponseDto(T data, String message) {
        super(ErrorCode.OK.getCode(), message);
        this.data = data;
    }

    public static <T> ApiDataResponseDto<T> of(T data) {
        return new ApiDataResponseDto<>(data);
    }

    public static <T> ApiDataResponseDto<T> of(T data, String message) {
        return new ApiDataResponseDto<>(data, message);
    }

    public static <T> ApiDataResponseDto<T> empty() {
        return new ApiDataResponseDto<>(null);
    }
}
