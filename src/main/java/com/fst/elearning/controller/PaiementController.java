package com.fst.elearning.controller;

import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.panier.PanierService;
import com.fst.elearning.service.InscriptionService;
import com.fst.elearning.service.UtilisateurService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/paiement")
public class PaiementController {

    private final PanierService panierService;
    private final UtilisateurService utilisateurService;
    private final InscriptionService inscriptionService;

    public PaiementController(PanierService panierService,
                              UtilisateurService utilisateurService,
                              InscriptionService inscriptionService) {
        this.panierService = panierService;
        this.utilisateurService = utilisateurService;
        this.inscriptionService = inscriptionService;
    }

    @GetMapping("/offres")
    public String offres() {
        return "paiement/offres";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model,
                           @RequestParam(required = false) String error) {
        model.addAttribute("items", panierService.getItems(session));
        model.addAttribute("total", panierService.total(session));
        if (error != null) model.addAttribute("erreur", error);
        return "paiement/checkout";
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('APPRENANT')")
    public String payer(HttpSession session,
                        @RequestParam String nomComplet,
                        @RequestParam String email,
                        @RequestParam String cardNumber,
                        @RequestParam String expiry,
                        @RequestParam String cvc) {

        if (panierService.count(session) == 0) {
            return "redirect:/paiement/checkout?error=Panier%20vide";
        }
        if (nomComplet == null || nomComplet.trim().length() < 3) {
            return "redirect:/paiement/checkout?error=Nom%20invalide";
        }
        if (email == null || !email.contains("@")) {
            return "redirect:/paiement/checkout?error=Email%20invalide";
        }
        if (cardNumber == null || cardNumber.replaceAll("\\s+", "").length() < 12) {
            return "redirect:/paiement/checkout?error=Num%C3%A9ro%20de%20carte%20invalide";
        }
        if (expiry == null || expiry.trim().length() < 4) {
            return "redirect:/paiement/checkout?error=Expiration%20invalide";
        }
        if (cvc == null || cvc.trim().length() < 3) {
            return "redirect:/paiement/checkout?error=CVC%20invalide";
        }

        Utilisateur user = utilisateurService.getUtilisateurConnecte();
        panierService.getItems(session).forEach(item -> {
            try {
                inscriptionService.inscrire(user, item.getCoursId());
            } catch (RuntimeException ignored) {
                // déjà inscrit -> on ignore
            }
        });

        panierService.clear(session);
        return "redirect:/paiement/success";
    }

    @GetMapping("/success")
    public String success() {
        return "paiement/success";
    }
}

