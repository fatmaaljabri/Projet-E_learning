package com.fst.elearning.controller;

import com.fst.elearning.dto.CourseCardDTO;
import com.fst.elearning.entity.Cours;
import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.service.*;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CoursController {

    private final CoursService coursService;
    private final InscriptionService inscriptionService;
    private final UtilisateurService utilisateurService;

    public CoursController(CoursService coursService,
                           InscriptionService inscriptionService,
                           UtilisateurService utilisateurService) {
        this.coursService = coursService;
        this.inscriptionService = inscriptionService;
        this.utilisateurService = utilisateurService;
    }

    // ===== Catalogue public =====
    @GetMapping("/catalogue")
    public String catalogue(
            @RequestParam(defaultValue = "") String titre,
            @RequestParam(defaultValue = "") String categorie,
            @RequestParam(defaultValue = "") String niveau,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<CourseCardDTO> cours = coursService.getCatalogue(titre, categorie, niveau, page, 9);
        model.addAttribute("cours", cours);
        model.addAttribute("categories", coursService.getAllCategories());
        model.addAttribute("niveaux", Cours.Niveau.values());
        model.addAttribute("titreRecherche", titre);
        model.addAttribute("categorieRecherche", categorie);
        model.addAttribute("niveauRecherche", niveau);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cours.getTotalPages());
        return "cours/catalogue";
    }

    // ===== Détail d'un cours =====
    @GetMapping("/cours/{id}/detail")
    public String detailCours(@PathVariable Long id, Model model, Authentication auth) {
        Cours cours = coursService.findById(id);
        model.addAttribute("cours", cours);

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            Utilisateur user = utilisateurService.getUtilisateurConnecte();
            model.addAttribute("estInscrit", inscriptionService.estInscrit(user, cours));
        } else {
            model.addAttribute("estInscrit", false);
        }
        return "cours/detail";
    }
}
