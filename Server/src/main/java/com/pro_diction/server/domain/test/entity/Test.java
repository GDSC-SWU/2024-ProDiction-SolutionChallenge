package com.pro_diction.server.domain.test.entity;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.model.Stage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

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
    @LastModifiedDate
    private LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Stage role;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
