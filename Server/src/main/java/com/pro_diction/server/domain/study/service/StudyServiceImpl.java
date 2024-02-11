package com.pro_diction.server.domain.study.service;

import com.pro_diction.server.domain.study.dto.StudyResponseDto;
import com.pro_diction.server.domain.study.dto.SubCategoryResponseDto;
import com.pro_diction.server.domain.study.entity.Category;
import com.pro_diction.server.domain.study.entity.Study;
import com.pro_diction.server.domain.study.entity.SubCategory;
import com.pro_diction.server.domain.study.exception.*;
import com.pro_diction.server.domain.study.repository.CategoryRepository;
import com.pro_diction.server.domain.study.repository.StudyRepository;
import com.pro_diction.server.domain.study.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {
    private final StudyRepository studyRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoryResponseDto> getSubCategoryList(Integer categoryId, boolean isFinalConsonant, Integer studyCount) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        if(categoryId != 2 && isFinalConsonant) throw new CategoryHasNotFinalConsonantException();

        List<SubCategory> subCategoryList = category.getChildrenSubCategory();

        return subCategoryList.stream()
                .filter(subCategory -> subCategory.isFinalConsonant() == isFinalConsonant)
                .map(subCategory -> SubCategoryResponseDto.builder()
                        .id(subCategory.getId())
                        .name(subCategory.getName())
                        .studyResponseDtoList(
                                subCategory.getChildrenStudy().subList(0, Math.min(subCategory.getChildrenStudy().size(), studyCount))
                                .stream()
                                .map(study -> StudyResponseDto.builder()
                                        .studyId(study.getId())
                                        .content(study.getContent())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudyResponseDto> getStudyList(Integer subCategoryId, Long parentStudyId) {
        List<Study> studyList;

        if(subCategoryId != null && parentStudyId == null) {
            SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElseThrow(SubCategoryNotFoundException::new);
            studyList = subCategory.getChildrenStudy();
        } else if(parentStudyId != null && subCategoryId == null) {
            Study parentStudy = studyRepository.findById(parentStudyId).orElseThrow(StudyNotFoundException::new);
            studyList = parentStudy.getChildrenStudy();

            if(parentStudy.getChildrenStudy().isEmpty()) {  // 받침 study의 부모 study가 아닌 경우
                throw new InvalidFinalConsonantParentStudy();
            }
        } else {    // subCategoryId, parentStudyId의 값이 모두 있거나 모두 없는 경우
            throw new OnlyOneParameterAllowedException();
        }

        return studyList.stream()
                .map(study -> StudyResponseDto.builder()
                        .studyId(study.getId())
                        .content(study.getContent())
                        .build())
                .collect(Collectors.toList());
    }
}
