package com.fst.elearning.controller;

import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.repository.UtilisateurRepository;
import com.fst.elearning.service.CoursService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FormateursController {

    private final UtilisateurRepository utilisateurRepository;
    private final CoursService coursService;

    public FormateursController(UtilisateurRepository utilisateurRepository, CoursService coursService) {
        this.utilisateurRepository = utilisateurRepository;
        this.coursService = coursService;
    }

    @GetMapping("/formateurs")
    public String liste(Model model) {
        List<Utilisateur> formateurs = utilisateurRepository.findByRole(Utilisateur.Role.FORMATEUR);
        model.addAttribute("formateurs", formateurs);
        model.addAttribute("categories", coursService.getAllCategories());
        return "formateurs/index";
    }
}

