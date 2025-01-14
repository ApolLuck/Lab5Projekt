package com.example.lab2projekt.domain.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
@Service
public class FileService {
    @Value("${files.location}")
    private String baseLocation;

    public String saveFile(MultipartFile multipartFile, String entityType, String entityId) throws IOException {
        // Tworzenie ścieżki: D://baseLocation/entityType/idEntity/fileName
        String uploadDir = String.format("%s/%s/%s", baseLocation, entityType, entityId);
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Tworzenie folderów, jeśli nie istnieją
        }

        // UWAGA !!! ta sciezka jest w formacie D:\\x\ co powodowało błąd, dalej musi byc zamiana na postać x/y/...
        String filePath = Path.of(uploadDir, multipartFile.getOriginalFilename()).toString();
        try (var fos = new FileOutputStream(filePath)) {
            fos.write(multipartFile.getBytes());
        }
        return filePath; // Zwracamy ścieżkę zapisanego pliku
    }

}
