package com.pro_diction.server.global.exception.auth;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class AccessTokenRequiredException extends GeneralException {
    public AccessTokenRequiredException() {
        super(ErrorCode.ACCESS_TOKEN_REQUIRED);
    }
}
