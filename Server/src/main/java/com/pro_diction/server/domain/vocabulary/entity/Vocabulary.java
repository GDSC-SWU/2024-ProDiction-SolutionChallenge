package com.pro_diction.server.domain.vocabulary.entity;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.study.entity.Category;
import com.pro_diction.server.domain.study.entity.Study;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Vocabulary")
@Entity
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vocabulary_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(optional = true)     // 카테고리가 기타일 경우 존재하지 않을 수 있다.
    @JoinColumn(name = "study_id")
    private Study study;

    private String content;     // 카테고리가 기타인 경우 존재한다.
}
