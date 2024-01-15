package com.pro_diction.server.domain.study.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    private String pronunciation_link;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
