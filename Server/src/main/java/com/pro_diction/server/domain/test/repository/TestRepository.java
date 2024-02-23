package com.pro_diction.server.domain.test.repository;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.test.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    Optional<Test> findOneByMember(Member member);
}
