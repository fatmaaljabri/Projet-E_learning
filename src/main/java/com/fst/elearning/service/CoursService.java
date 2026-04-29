package com.fst.elearning.service;

import com.fst.elearning.dto.CourseCardDTO;
import com.fst.elearning.entity.Cours;
import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.repository.CoursRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class CoursService {

    private final CoursRepository coursRepository;
    private final FileStorageService fileStorageService;

    public CoursService(CoursRepository coursRepository, FileStorageService fileStorageService) {
        this.coursRepository = coursRepository;
        this.fileStorageService = fileStorageService;
    }

    public Page<CourseCardDTO> getCatalogue(String titre, String categorie, String niveauStr, int page, int size) {
        Cours.Niveau niveau = null;
        if (niveauStr != null && !niveauStr.isBlank()) {
            try { niveau = Cours.Niveau.valueOf(niveauStr); } catch (Exception ignored) {}
        }
        String titreParam = (titre != null && !titre.isBlank()) ? titre : null;
        String catParam   = (categorie != null && !categorie.isBlank()) ? categorie : null;

        Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreation").descending());
        Page<Cours> coursPaged = coursRepository.findCatalogueFiltre(titreParam, catParam, niveau, pageable);
        return coursPaged.map(this::toDTO);
    }

    public Cours findById(Long id) {
        return coursRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cours non trouvé : " + id));
    }

    public Cours creer(Cours cours, MultipartFile image, Utilisateur formateur) throws IOException {
        cours.setFormateur(formateur);
        if (image != null && !image.isEmpty()) {
            cours.setImageUrl(fileStorageService.sauvegarderFichier(image));
        }
        return coursRepository.save(cours);
    }

    public Cours modifier(Long id, Cours coursModif, MultipartFile image) throws IOException {
        Cours cours = findById(id);
        cours.setTitre(coursModif.getTitre());
        cours.setDescription(coursModif.getDescription());
        cours.setCategorie(coursModif.getCategorie());
        cours.setNiveau(coursModif.getNiveau());
        cours.setActif(coursModif.isActif());
        if (image != null && !image.isEmpty()) {
            if (cours.getImageUrl() != null) fileStorageService.supprimerFichier(cours.getImageUrl());
            cours.setImageUrl(fileStorageService.sauvegarderFichier(image));
        }
        return coursRepository.save(cours);
    }

    public void supprimer(Long id) {
        Cours cours = findById(id);
        if (cours.getImageUrl() != null) fileStorageService.supprimerFichier(cours.getImageUrl());
        coursRepository.delete(cours);
    }

    public Page<Cours> getCoursByFormateur(Utilisateur formateur, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreation").descending());
        return coursRepository.findByFormateurOrderByDateCreationDesc(formateur, pageable);
    }

    public List<String> getAllCategories() {
        return coursRepository.findAllCategories();
    }

    private CourseCardDTO toDTO(Cours c) {
        return CourseCardDTO.builder()
            .id(c.getId())
            .titre(c.getTitre())
            .description(c.getDescription() != null && c.getDescription().length() > 150
                ? c.getDescription().substring(0, 150) + "..." : c.getDescription())
            .categorie(c.getCategorie())
            .niveau(c.getNiveau())
            .imageUrl(c.getImageUrl())
            .formateurNom(c.getFormateur().getNomComplet())
            .nombreModules(c.getModules() != null ? c.getModules().size() : 0)
            .nombreInscrits(c.getNombreInscrits())
            .build();
    }
}
