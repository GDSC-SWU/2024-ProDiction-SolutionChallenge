package com.pro_diction.server.domain.vocabulary.exception;

import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;

public class DuplicatedVocabularyException extends GeneralException {
    public DuplicatedVocabularyException() {
        super(ErrorCode.DUPLICATED_VOCABULARY);
    }
}
