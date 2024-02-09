package com.pro_diction.server.domain.study.exception;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class CategoryHasNotFinalConsonantException extends GeneralException {
    public CategoryHasNotFinalConsonantException() {
        super(ErrorCode.CATEGORY_HAS_NOT_FINAL_CONSONANT);
    }
}
