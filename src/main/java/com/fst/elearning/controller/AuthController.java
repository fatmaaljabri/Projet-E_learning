package com.fst.elearning.controller;

import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.service.EmailVerificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final EmailVerificationService emailVerificationService;

    public AuthController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) model.addAttribute("erreur", "Connexion impossible. Vérifiez vos identifiants ou activez votre compte par email.");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        model.addAttribute("roles", new Utilisateur.Role[]{Utilisateur.Role.APPRENANT, Utilisateur.Role.FORMATEUR, Utilisateur.Role.PARENT});
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Utilisateur utilisateur,
                           @RequestParam String confirmerMotDePasse,
                           Model model) {
        try {
            if (utilisateur.getMotDePasse() == null || !utilisateur.getMotDePasse().equals(confirmerMotDePasse)) {
                throw new RuntimeException("Les mots de passe ne correspondent pas");
            }
            emailVerificationService.createAndSend(utilisateur);
            return "redirect:/auth/verify-code?verifySent=true&email=" + utilisateur.getEmail().replace("@", "%40");
        } catch (Exception e) {
            model.addAttribute("erreur", e.getMessage());
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("roles", new Utilisateur.Role[]{Utilisateur.Role.APPRENANT, Utilisateur.Role.FORMATEUR, Utilisateur.Role.PARENT});
            return "auth/register";
        }
    }

    @GetMapping("/verify")
    public String verify(@RequestParam String token) {
        try {
            emailVerificationService.verify(token);
            return "redirect:/auth/login?verified=true";
        } catch (RuntimeException e) {
            return "redirect:/auth/verify-code?error=" + e.getMessage().replace(" ", "%20");
        }
    }

    @GetMapping("/verify-code")
    public String verifyCodePage(@RequestParam(required = false) String error,
                                 @RequestParam(required = false) String email,
                                 @RequestParam(required = false) String verifySent,
                                 Model model) {
        if (error != null) model.addAttribute("erreur", error);
        if (email != null) model.addAttribute("email", email);
        if (verifySent != null) model.addAttribute("verifySent", true);
        return "auth/verify-code";
    }

    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam String email, @RequestParam String code, Model model) {
        try {
            emailVerificationService.verifyByCode(email, code);
            return "redirect:/auth/login?verified=true";
        } catch (RuntimeException e) {
            model.addAttribute("erreur", e.getMessage());
            model.addAttribute("email", email);
            return "auth/verify-code";
        }
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
}
