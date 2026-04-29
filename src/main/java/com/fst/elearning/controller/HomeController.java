package com.fst.elearning.controller;

import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.service.UtilisateurService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UtilisateurService utilisateurService;

    public HomeController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/catalogue";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/catalogue";
        }
        Utilisateur user = utilisateurService.getUtilisateurConnecte();
        return switch (user.getRole()) {
            case ADMIN      -> "redirect:/admin/dashboard";
            case FORMATEUR  -> "redirect:/formateur/dashboard";
            case APPRENANT  -> "redirect:/apprenant/dashboard";
        };
    }
}
