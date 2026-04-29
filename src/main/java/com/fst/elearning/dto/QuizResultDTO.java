package com.fst.elearning.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuizResultDTO {
    private Long quizId;
    private String quizTitre;
    private int score;
    private int totalQuestions;
    private double pourcentage;
    private boolean reussi;
}
