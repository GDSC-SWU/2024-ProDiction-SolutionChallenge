package com.pro_diction.server.domain.search.exception;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class KeywordRequiredException extends GeneralException {
    public KeywordRequiredException() {
        super(ErrorCode.KEYWORD_REQUIRED);
    }
}
