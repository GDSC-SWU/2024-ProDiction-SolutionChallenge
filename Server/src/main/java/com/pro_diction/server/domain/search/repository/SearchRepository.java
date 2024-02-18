package com.pro_diction.server.domain.search.repository;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.search.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {
    @Query("SELECT s FROM Search s WHERE s.member = :member ORDER BY s.id DESC LIMIT 5")
    List<Search> findAllByMemberOrderById(Member member);
}
