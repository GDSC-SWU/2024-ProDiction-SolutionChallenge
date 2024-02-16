package com.pro_diction.server.domain.study.service;

import com.pro_diction.server.domain.study.dto.*;
import com.pro_diction.server.domain.study.entity.Category;
import com.pro_diction.server.domain.study.entity.Study;
import com.pro_diction.server.domain.study.entity.SubCategory;
import com.pro_diction.server.domain.study.exception.*;
import com.pro_diction.server.domain.study.repository.CategoryRepository;
import com.pro_diction.server.domain.study.repository.StudyRepository;
import com.pro_diction.server.domain.study.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {
    private final StudyRepository studyRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Value("${PRODICTION_AI_API_URL}")
    private String PRODICTION_AI_API_URL;

    @Override
    @Transactional(readOnly = true)
    public List<StudyResponseDto> getStudyList(Integer subCategoryId, Long parentStudyId) {
        List<Study> studyList = getStudyListBySubCategoryOrParentStudy(subCategoryId, parentStudyId);

        return studyList.stream()
                .map(study -> StudyResponseDto.toResponse(study.getId(),
                        parentStudyId == null ? study.getContent() : study.getSubCategory().getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DetailStudyResponseDto getDetailStudy(Long id) {
        Study study = studyRepository.findById(id).orElseThrow(StudyNotFoundException::new);

        return DetailStudyResponseDto.builder()
                .studyId(study.getId())
                .content(study.getContent())
                .pronunciation(study.getPronunciation())
                .splitPronunciation(splitPronunciation(study.getPronunciation()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public StudyResultDto getStudyResult(MultipartFile multipartFile, Long id) {
        Study study = studyRepository.findById(id).orElseThrow(StudyNotFoundException::new);
        SttResultDto sttResultDto = sttPronunciation(multipartFile);

        return StudyResultDto.toResponse(
                study.getId(), sttResultDto.getResult(), sttResultDto.getResult_splited()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoryResponseDto> getSubCategoryList(Integer categoryId, Integer studyCount) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        List<SubCategory> subCategoryList = category.getChildrenSubCategory();

        return subCategoryList.stream()
                .filter(subCategory -> !subCategory.isFinalConsonant())
                .map(subCategory -> SubCategoryResponseDto.builder()
                        .id(subCategory.getId())
                        .name(subCategory.getName())
                        .studyResponseDtoList(
                                subCategory.getChildrenStudy()
                                        .subList(0, Math.min(subCategory.getChildrenStudy().size(), studyCount))
                                        .stream()
                                        .map(study -> StudyResponseDto.toResponse(study.getId(), study.getContent()))
                                        .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    private String splitPronunciation(String pronunciation) {
        RestTemplate restTemplate = new RestTemplate();
        String splitPronunciation =
                restTemplate.getForObject(PRODICTION_AI_API_URL + "/splitjamos?text={pronunciation}", String.class, pronunciation);

        if(splitPronunciation != null) {
            return splitPronunciation.replace("\"", "");
        } else {
            throw new NullPointerException();
        }
    }

    private SttResultDto sttPronunciation(MultipartFile file) {
        try {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(10000);
            factory.setReadTimeout(10000);
            RestTemplate restTemplate = new RestTemplate(factory);

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 파일을 MultiValueMap에 추가
            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("file", file.getResource());

            // Request Header, Body 요청 구성
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<SttResultDto> responseEntity =
                    restTemplate.postForEntity(PRODICTION_AI_API_URL + "/speechtotext_syllables", requestEntity, SttResultDto.class);

            return responseEntity.getBody();
        } catch (Exception e) {
            return SttResultDto.builder()
                    .result(e.getMessage())
                    .result_splited(e.getMessage())
                    .build();
        }
    }

    private List<Study> getStudyListBySubCategoryOrParentStudy(Integer subCategoryId, Long parentStudyId) {
        List<Study> studyList;

        if (subCategoryId != null && parentStudyId == null) {
            SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                    .orElseThrow(SubCategoryNotFoundException::new);
            studyList = subCategory.getChildrenStudy();
        } else if (parentStudyId != null && subCategoryId == null) {
            Study parentStudy = studyRepository.findById(parentStudyId)
                    .orElseThrow(StudyNotFoundException::new);
            studyList = parentStudy.getChildrenStudy();

            if (parentStudy.getChildrenStudy().isEmpty()) {  // 받침 Study의 부모 Study가 아닌 경우
                throw new InvalidFinalConsonantParentStudyException();
            }
        } else {    // subCategoryId, parentStudyId의 값이 모두 있거나 모두 없는 경우
            throw new OnlyOneParameterAllowedException();
        }

        return studyList;
    }
}
