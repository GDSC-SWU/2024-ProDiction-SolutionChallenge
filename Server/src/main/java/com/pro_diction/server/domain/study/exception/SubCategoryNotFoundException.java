package com.pro_diction.server.domain.study.exception;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class SubCategoryNotFoundException extends GeneralException {
    public SubCategoryNotFoundException() {
        super(ErrorCode.SUBCATEGORY_NOT_FOUND);
    }
}
