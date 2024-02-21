package com.pro_diction.server.domain.test.entity;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.model.Stage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Test")
@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Long id;

    @CreatedDate
    private LocalDate createdDate;

    @LastModifiedDate
    private LocalDate modifiedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Stage stage;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Test(Member member) {
        this.member = member;
    }

    public void update(Stage stage) {
        this.stage = stage;
    }
}
