package com.pro_diction.server.domain.study.repository;

import com.pro_diction.server.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    @Query("SELECT s FROM Study s WHERE s.subCategory.category.id = :categoryId ORDER BY rand() LIMIT 3")
    List<Study> getStudyByCategoryId(@Param("categoryId") Integer categoryId);
}