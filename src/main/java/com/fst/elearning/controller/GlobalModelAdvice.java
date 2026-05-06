package com.fst.elearning.controller;

import com.fst.elearning.panier.PanierService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    private final PanierService panierService;

    public GlobalModelAdvice(PanierService panierService) {
        this.panierService = panierService;
    }

    @ModelAttribute("panierCount")
    public Integer panierCount(HttpSession session) {
        if (session == null) return 0;
        return panierService.count(session);
    }
}

