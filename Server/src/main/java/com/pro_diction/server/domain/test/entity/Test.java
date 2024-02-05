package com.pro_diction.server.domain.test.entity;

import com.pro_diction.server.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Test")
@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer stage;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
