package com.fst.elearning.controller;

import com.fst.elearning.entity.Lecon;
import com.fst.elearning.service.LeconService;
import com.fst.elearning.service.PdfSupportService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class LeconPdfController {

    private final LeconService leconService;
    private final PdfSupportService pdfSupportService;

    public LeconPdfController(LeconService leconService, PdfSupportService pdfSupportService) {
        this.leconService = leconService;
        this.pdfSupportService = pdfSupportService;
    }

    @GetMapping(value = "/lecons/{id}/pdf/text", produces = "text/plain; charset=utf-8")
    public ResponseEntity<String> pdfText(@PathVariable Long id) throws IOException {
        Lecon lecon = leconService.findById(id);
        if (lecon.getPdfUrl() == null) {
            return ResponseEntity.badRequest().body("Aucun PDF");
        }
        String text = pdfSupportService.extractTextFromUploadsUrl(lecon.getPdfUrl());
        if (text.isBlank()) text = "(PDF vide ou texte non extractible)";
        return ResponseEntity.ok(text);
    }

    @PostMapping(
            value = "/lecons/{id}/pdf/translate",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = "text/plain; charset=utf-8"
    )
    public ResponseEntity<String> translate(@PathVariable Long id,
                                            @RequestParam(defaultValue = "fr") String to,
                                            @RequestBody String text) throws IOException {
        Lecon lecon = leconService.findById(id);
        if (lecon.getPdfUrl() == null) {
            return ResponseEntity.badRequest().body("Aucun PDF");
        }
        String target = switch (to.toLowerCase()) {
            case "en" -> "en";
            case "ar" -> "ar";
            default -> "fr";
        };
        // hypothèse: contenus surtout FR -> traduire depuis FR
        String translated = pdfSupportService.translateViaMyMemory(text, "fr", target);
        return ResponseEntity.ok(translated);
    }
}

