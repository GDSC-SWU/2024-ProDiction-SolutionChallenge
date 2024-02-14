package com.pro_diction.server.domain.study.exception;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class OnlyOneParameterAllowedException extends GeneralException {
    public OnlyOneParameterAllowedException() {
        super(ErrorCode.ONLY_ONE_PARAMETER_ALLOWED);
    }
}
