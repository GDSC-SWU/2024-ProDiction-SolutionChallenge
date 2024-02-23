package com.pro_diction.server.domain.member.entity;

import com.pro_diction.server.domain.model.Role;
import com.pro_diction.server.domain.test.entity.Test;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Member")
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull
    private String googleEmail;

    @NotNull
    private String googleNickname;

    @NotNull
    private String googleProfile;

    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "member")
    private Test test;

    public void update(Integer age) {
        this.age = age;
    }
}
