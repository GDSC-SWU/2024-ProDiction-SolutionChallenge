package com.pro_diction.server.domain.vocabulary.exception;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class VocabularyNotFoundException extends GeneralException {
    public VocabularyNotFoundException() {
        super(ErrorCode.VOCABULARY_NOT_FOUND);
    }
}
