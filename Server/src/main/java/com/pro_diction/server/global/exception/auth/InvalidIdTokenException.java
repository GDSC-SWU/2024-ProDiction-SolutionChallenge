package com.pro_diction.server.global.exception.auth;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class InvalidIdTokenException extends GeneralException {
    public InvalidIdTokenException() {
        super(ErrorCode.INVALID_ID_TOKEN);
    }
}
