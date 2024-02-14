package com.pro_diction.server.domain.study.exception;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class CategoryNotFoundException extends GeneralException {
    public CategoryNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND);
    }
}
