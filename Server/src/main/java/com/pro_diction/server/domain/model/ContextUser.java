package com.pro_diction.server.domain.model;

import com.pro_diction.server.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class ContextUser extends User {
    private final Member member;

    public ContextUser(Member member) {
        super(member.getGoogle_email(), member.getGoogle_nickname(), Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())));
        this.member = member;
    }
}

