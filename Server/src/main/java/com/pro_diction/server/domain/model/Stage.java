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

    public static Stage getByLevel(Integer level) {
        for (Stage stage : Stage.values()) {
            if (stage.getLevel().equals(level)) {
                return stage;
            }
        }
        throw new IllegalArgumentException("No Stage found for level: " + level);
    }
}
