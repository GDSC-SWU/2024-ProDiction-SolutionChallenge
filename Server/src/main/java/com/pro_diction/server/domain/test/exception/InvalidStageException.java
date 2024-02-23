package com.pro_diction.server.domain.test.exception;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class InvalidStageException extends GeneralException {
    public InvalidStageException() {
        super(ErrorCode.INVALID_STAGE_NUM);
    }
}
