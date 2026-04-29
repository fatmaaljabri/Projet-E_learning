package com.fst.elearning.dto;

import com.fst.elearning.entity.Cours;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CourseCardDTO {
    private Long id;
    private String titre;
    private String description;
    private String categorie;
    private Cours.Niveau niveau;
    private String imageUrl;
    private String formateurNom;
    private int nombreModules;
    private int nombreInscrits;
}
