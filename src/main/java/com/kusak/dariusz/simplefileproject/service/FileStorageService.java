package com.kusak.dariusz.simplefileproject.service;

import com.kusak.dariusz.simplefileproject.config.FileStorageConfig;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new RuntimeException("Could not create directory for " + fileStorageConfig, e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            validateFileName(fileName);
            Files.copy(file.getInputStream(), fileStorageLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Error occured with storage file " + file);
        }
    }

    public Resource loadFileAsResource(String fileName) throws FileNotFoundException {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) throw new FileNotFoundException("File not found " + fileName);
            return resource;
        } catch (Exception e) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }

    private void validateFileName(String fileName) {
        if (fileName.contains("..")) throw new InvalidFileNameException(fileName, "Invalid file name");
    }


}
