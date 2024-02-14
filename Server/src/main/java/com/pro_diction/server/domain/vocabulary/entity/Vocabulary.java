package com.pro_diction.server.domain.vocabulary.entity;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.study.entity.Study;
import com.pro_diction.server.domain.vocabulary.dto.SaveVocabularyResponseDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Vocabulary")
@Entity
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vocabulary_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "study_id")
    private Study study;

    public SaveVocabularyResponseDto toResponse() {
        return SaveVocabularyResponseDto.builder()
                .vocabularyId(getId())
                .studyId(getStudy().getId())
                .memberId(getMember().getId())
                .build();
    }
}
