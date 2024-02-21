package com.pro_diction.server.domain.model;

import com.pro_diction.server.domain.test.exception.InvalidStageException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Stage {
    PHONEME(1, "Phoneme"),
    SYLLABLE(2, "Syllable"),
    WORD(3, "Word"),
    PHRASE(4, "Phrase"),
    SENTENCE(5, "Sentence");

    private final Integer level;
    private final String title;

    public static Stage getByLevel(Integer level) {
        for (Stage stage : Stage.values()) {
            if (stage.getLevel().equals(level)) {
                return stage;
            }
        }
        throw new InvalidStageException();
    }
}
