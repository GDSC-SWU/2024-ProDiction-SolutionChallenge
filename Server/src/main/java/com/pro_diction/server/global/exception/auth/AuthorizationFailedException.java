package com.pro_diction.server.global.exception.auth;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class AuthorizationFailedException extends GeneralException {
    public AuthorizationFailedException() {
        super(ErrorCode.AUTHORIZATION_FAILED);
    }
}
