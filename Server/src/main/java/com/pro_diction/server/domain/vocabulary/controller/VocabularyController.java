package com.pro_diction.server.domain.vocabulary.controller;

import com.pro_diction.server.domain.model.ContextUser;
import com.pro_diction.server.domain.vocabulary.dto.SaveVocabularyResponseDto;
import com.pro_diction.server.domain.vocabulary.service.VocabularyService;
import com.pro_diction.server.global.common.ApiDataResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/vocabulary")
@Slf4j
public class VocabularyController {
    private final VocabularyService vocabularyService;

    @PostMapping("/{id}")
    public ApiDataResponseDto<SaveVocabularyResponseDto> saveVocabulary(@PathVariable @NotNull Long id,
                                                                       @AuthenticationPrincipal ContextUser contextUser) {

        return ApiDataResponseDto.of(vocabularyService.saveVocabulary(id, contextUser.getMember()));
    }
}
