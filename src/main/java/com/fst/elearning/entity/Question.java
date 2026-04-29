package com.fst.elearning.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enonce;

    @Column(nullable = false, length = 500)
    private String choixA;

    @Column(nullable = false, length = 500)
    private String choixB;

    @Column(nullable = false, length = 500)
    private String choixC;

    @Column(nullable = false, length = 500)
    private String choixD;

    @Column(nullable = false, length = 1)
    private String bonneReponse; // A, B, C ou D

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
}
