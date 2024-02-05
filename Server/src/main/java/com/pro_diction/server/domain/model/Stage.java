package com.pro_diction.server.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Stage {
    PHONEME(1, "phoneme"),
    SYLLABLE(2, "syllable"),
    WORD(3, "word"),
    PHRASE(4, "phrase"),
    SENTENCE(5, "sentence");

    private final Integer level;
    private final String title;
}
