package com.fst.elearning.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reponses_apprenants")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReponseApprenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apprenant_id", nullable = false)
    private Utilisateur apprenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private int totalQuestions;

    @Column(nullable = false)
    private LocalDateTime datePassage;

    @PrePersist
    public void prePersist() {
        datePassage = LocalDateTime.now();
    }

    public double getPourcentage() {
        if (totalQuestions == 0) return 0;
        return (double) score / totalQuestions * 100;
    }
}
