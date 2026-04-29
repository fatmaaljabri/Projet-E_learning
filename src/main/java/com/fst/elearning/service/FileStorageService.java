package com.fst.elearning.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String sauvegarderFichier(MultipartFile fichier) throws IOException {
        if (fichier == null || fichier.isEmpty()) return null;

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = fichier.getOriginalFilename();
        String extension = originalName != null && originalName.contains(".")
            ? originalName.substring(originalName.lastIndexOf("."))
            : ".jpg";
        String nomFichier = UUID.randomUUID() + extension;

        Path destination = uploadPath.resolve(nomFichier);
        Files.copy(fichier.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + nomFichier;
    }

    public void supprimerFichier(String url) {
        if (url == null || !url.startsWith("/uploads/")) return;
        try {
            String nomFichier = url.substring("/uploads/".length());
            Path filePath = Paths.get(uploadDir).toAbsolutePath().resolve(nomFichier);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log silencieux
        }
    }
}
