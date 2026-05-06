package com.fst.elearning.panier;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PanierItem {
    private Long coursId;
    private String titre;
    private BigDecimal prix;
    private String imageUrl;
}

