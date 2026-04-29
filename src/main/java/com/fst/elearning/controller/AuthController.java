package com.fst.elearning.controller;

import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.service.UtilisateurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UtilisateurService utilisateurService;

    public AuthController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) model.addAttribute("erreur", "Email ou mot de passe incorrect.");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        model.addAttribute("roles", new Utilisateur.Role[]{Utilisateur.Role.APPRENANT, Utilisateur.Role.FORMATEUR});
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Utilisateur utilisateur, Model model) {
        try {
            utilisateurService.inscrire(utilisateur);
            return "redirect:/auth/login?registered=true";
        } catch (RuntimeException e) {
            model.addAttribute("erreur", e.getMessage());
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("roles", new Utilisateur.Role[]{Utilisateur.Role.APPRENANT, Utilisateur.Role.FORMATEUR});
            return "auth/register";
        }
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
}
