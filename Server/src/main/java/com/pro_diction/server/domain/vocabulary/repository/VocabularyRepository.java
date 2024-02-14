package com.pro_diction.server.domain.vocabulary.repository;

import com.pro_diction.server.domain.vocabulary.entity.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    Optional<Vocabulary> findOneByStudy_idAndMember_id(Long studyId, Long memberId);

    List<Vocabulary> findAllByMember_id(Long memberId);
}
