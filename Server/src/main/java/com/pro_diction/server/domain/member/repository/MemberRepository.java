package com.pro_diction.server.domain.member.repository;

import com.pro_diction.server.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findOneByGoogleEmail(String googleEmail);
}
