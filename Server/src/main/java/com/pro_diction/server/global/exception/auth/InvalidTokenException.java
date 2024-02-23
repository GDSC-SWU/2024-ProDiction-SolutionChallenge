package com.pro_diction.server.global.exception.auth;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class InvalidTokenException extends GeneralException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
