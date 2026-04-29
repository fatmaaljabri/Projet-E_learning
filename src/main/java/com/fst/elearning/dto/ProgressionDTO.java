package com.fst.elearning.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProgressionDTO {
    private Long coursId;
    private String coursTitre;
    private int pourcentage;
    private int leconsCompletees;
    private int totalLecons;
    private String statut;
}
