package com.pro_diction.server.domain.search.entity;

import com.pro_diction.server.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Search")
@Entity
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private Long id;

    @NotNull
    private String searchContent;

    @CreationTimestamp
    private LocalDateTime searchTime;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
