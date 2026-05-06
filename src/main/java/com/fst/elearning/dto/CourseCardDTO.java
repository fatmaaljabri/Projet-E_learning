package com.fst.elearning.dto;

import com.fst.elearning.entity.Cours;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CourseCardDTO {
    private Long id;
    private String titre;
    private String description;
    private String categorie;
    private Cours.Niveau niveau;
    private String imageUrl;
    private BigDecimal prix;
    private String formateurNom;
    private int nombreModules;
    private int nombreInscrits;
}
