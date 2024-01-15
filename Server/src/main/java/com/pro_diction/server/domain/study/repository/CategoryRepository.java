package com.pro_diction.server.domain.study.repository;

import com.pro_diction.server.domain.study.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
