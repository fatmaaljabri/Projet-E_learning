package com.fst.elearning.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lecons")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Lecon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private String pdfUrl;

    @Column(nullable = false)
    private int ordre;

    private int dureeMin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @OneToMany(mappedBy = "lecon")
    @Builder.Default
    private List<ProgressionLecon> progressions = new ArrayList<>();
}