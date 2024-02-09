package com.pro_diction.server.domain.study.service;

import com.pro_diction.server.domain.study.dto.SubCategoryResponseDto;
import com.pro_diction.server.domain.study.entity.Category;
import com.pro_diction.server.domain.study.entity.SubCategory;
import com.pro_diction.server.domain.study.exception.CategoryHasNotFinalConsonantException;
import com.pro_diction.server.domain.study.exception.CategoryNotFoundException;
import com.pro_diction.server.domain.study.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoryResponseDto> getSubCategoryList(Integer categoryId, boolean isFinalConsonant) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        if(categoryId != 2 && isFinalConsonant) throw new CategoryHasNotFinalConsonantException();

        List<SubCategory> subCategoryList = category.getChildrenSubCategory();

        return subCategoryList.stream()
                .filter(subCategory -> subCategory.isFinalConsonant() == isFinalConsonant)
                .map(subCategory -> SubCategoryResponseDto.builder()
                        .id(subCategory.getId())
                        .name(subCategory.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
