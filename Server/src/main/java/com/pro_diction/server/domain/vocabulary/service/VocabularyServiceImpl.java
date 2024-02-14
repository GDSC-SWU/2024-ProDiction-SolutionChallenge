package com.pro_diction.server.domain.vocabulary.service;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.member.exception.MemberNotFoundException;
import com.pro_diction.server.domain.member.repository.MemberRepository;
import com.pro_diction.server.domain.study.entity.Category;
import com.pro_diction.server.domain.study.entity.Study;
import com.pro_diction.server.domain.study.exception.CategoryNotFoundException;
import com.pro_diction.server.domain.study.exception.StudyNotFoundException;
import com.pro_diction.server.domain.study.repository.CategoryRepository;
import com.pro_diction.server.domain.study.repository.StudyRepository;
import com.pro_diction.server.domain.vocabulary.dto.SaveVocabularyResponseDto;
import com.pro_diction.server.domain.vocabulary.dto.VocabularyResponseDto;
import com.pro_diction.server.domain.vocabulary.entity.Vocabulary;
import com.pro_diction.server.domain.vocabulary.exception.DuplicatedVocabularyException;
import com.pro_diction.server.domain.vocabulary.exception.VocabularyNotFoundException;
import com.pro_diction.server.domain.vocabulary.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VocabularyServiceImpl implements VocabularyService {
    private final VocabularyRepository vocabularyRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VocabularyResponseDto> getVocabularyList(Integer categoryId, Member member) {
        member = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        List<Vocabulary> vocabularyList = vocabularyRepository.findAllByMember_id(member.getId());

        return vocabularyList.stream()
                .filter(vocabulary -> vocabulary.getStudy().getSubCategory().getCategory() == category)
                .map(vocabulary -> VocabularyResponseDto.builder()
                        .vocabularyId(vocabulary.getId())
                        .studyId(vocabulary.getStudy().getId())
                        .content(vocabulary.getStudy().getContent())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SaveVocabularyResponseDto saveVocabulary(Long studyId, Member member) {
        validateVocabulary(studyId, member.getId());
        member = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        Study study = studyRepository.findById(studyId).orElseThrow(StudyNotFoundException::new);

        Vocabulary vocabulary = Vocabulary.builder()
                .member(member)
                .study(study)
                .build();
        vocabularyRepository.save(vocabulary);

        return vocabulary.toResponse();
    }

    @Override
    @Transactional
    public SaveVocabularyResponseDto deleteVocabulary(Long vocabularyId, Member member) {
        Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(VocabularyNotFoundException::new);
        vocabularyRepository.delete(vocabulary);

        return vocabulary.toResponse();
    }

    private void validateVocabulary(Long studyId, Long memberId) {
        boolean isDuplicated = vocabularyRepository
                .findOneByStudy_idAndMember_id(studyId, memberId).isPresent();

        if(isDuplicated)    throw new DuplicatedVocabularyException();
    }
}
