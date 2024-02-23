package com.pro_diction.server.domain.study.exception;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class StudyNotFoundException extends GeneralException {
    public StudyNotFoundException() {
        super(ErrorCode.STUDY_NOT_FOUND);
    }
}
