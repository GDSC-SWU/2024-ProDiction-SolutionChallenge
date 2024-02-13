package com.pro_diction.server.domain.study.exception;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class InvalidFinalConsonantParentStudyException extends GeneralException {
    public InvalidFinalConsonantParentStudyException() {
        super(ErrorCode.INVALID_FINAL_CONSONANT_PARENT_STUDY);
    }
}
