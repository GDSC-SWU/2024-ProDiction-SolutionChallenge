package com.pro_diction.server.domain.search.exception;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class SearchNotFoundException extends GeneralException {
    public SearchNotFoundException() {
        super(ErrorCode.SEARCH_NOT_FOUND);
    }
}
