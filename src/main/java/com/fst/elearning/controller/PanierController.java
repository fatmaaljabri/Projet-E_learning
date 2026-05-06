package com.fst.elearning.controller;

import com.fst.elearning.panier.PanierService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PanierController {

    private final PanierService panierService;

    public PanierController(PanierService panierService) {
        this.panierService = panierService;
    }

    @GetMapping("/panier")
    public String viewPanier(HttpSession session, Model model) {
        model.addAttribute("items", panierService.getItems(session));
        model.addAttribute("total", panierService.total(session));
        return "panier/index";
    }

    @PostMapping("/panier/add/{coursId}")
    public String add(@PathVariable Long coursId,
                      @RequestHeader(value = "Referer", required = false) String referer,
                      HttpSession session) {
        panierService.addCours(session, coursId);
        return "redirect:" + (referer != null ? referer : "/panier");
    }

    @PostMapping("/panier/remove/{coursId}")
    public String remove(@PathVariable Long coursId, HttpSession session) {
        panierService.removeCours(session, coursId);
        return "redirect:/panier";
    }

    @PostMapping("/panier/clear")
    public String clear(HttpSession session) {
        panierService.clear(session);
        return "redirect:/panier";
    }
}

