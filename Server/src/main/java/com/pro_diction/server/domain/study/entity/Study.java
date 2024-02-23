package com.pro_diction.server.domain.study.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Study")
@Entity
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    @NotNull
    private String content;

    @NotNull
    private String pronunciation;

    @Null
    private Integer relevantNum;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private SubCategory subCategory;

    // syllable 학습을 위한 재귀 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_study_id")
    private Study parentStudy;

    @OneToMany(mappedBy = "parentStudy")
    private List<Study> childrenStudy;
}