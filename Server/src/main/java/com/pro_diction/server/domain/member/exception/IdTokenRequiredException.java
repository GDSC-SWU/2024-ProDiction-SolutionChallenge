package com.pro_diction.server.domain.member.exception;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class IdTokenRequiredException extends GeneralException {
    public IdTokenRequiredException() {
        super(ErrorCode.ID_TOKEN_REQUIRED);
    }
}
