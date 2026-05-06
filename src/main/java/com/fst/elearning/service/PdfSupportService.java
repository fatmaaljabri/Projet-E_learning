package com.fst.elearning.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PdfSupportService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String extractTextFromUploadsUrl(String uploadsUrl) throws IOException {
        if (uploadsUrl == null || !uploadsUrl.startsWith("/uploads/")) {
            throw new IOException("PDF introuvable");
        }
        String fileName = uploadsUrl.substring("/uploads/".length());
        Path filePath = Paths.get(uploadDir).toAbsolutePath().resolve(fileName);

        try (PDDocument doc = PDDocument.load(filePath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            return text != null ? text.trim() : "";
        }
    }

    /**
     * Traduction simple via MyMemory (sans clé). En cas d'échec réseau, on remonte une IOException.
     */
    public String translateViaMyMemory(String text, String from, String to) throws IOException {
        String q = URLEncoder.encode(text, StandardCharsets.UTF_8);
        String langpair = URLEncoder.encode(from + "|" + to, StandardCharsets.UTF_8);
        URI uri = URI.create("https://api.mymemory.translated.net/get?q=" + q + "&langpair=" + langpair);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(uri)
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (res.statusCode() < 200 || res.statusCode() >= 300) {
                throw new IOException("Erreur traduction HTTP " + res.statusCode());
            }
            String body = res.body() != null ? res.body() : "";
            // parsing léger pour "translatedText" (évite ajouter une dépendance JSON)
            String marker = "\"translatedText\":\"";
            int idx = body.indexOf(marker);
            if (idx < 0) return text;
            int start = idx + marker.length();
            int end = body.indexOf('"', start);
            if (end < 0) return text;
            String translated = body.substring(start, end);
            translated = translated.replace("\\n", "\n").replace("\\u003d", "=").replace("\\u0026", "&");
            translated = translated.replace("\\\"", "\"").replace("\\\\", "\\");
            return translated.trim().isEmpty() ? text : translated;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Traduction interrompue");
        }
    }
}

