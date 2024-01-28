package com.pro_diction.server.domain.search.repository;

import com.pro_diction.server.domain.search.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {
}
