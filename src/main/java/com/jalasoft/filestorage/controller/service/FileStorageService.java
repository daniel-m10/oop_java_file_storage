package com.jalasoft.filestorage.controller.service;

import com.jalasoft.filestorage.controller.exceptions.FileNotFoundException;
import com.jalasoft.filestorage.controller.exceptions.InvalidSequence;
import com.jalasoft.filestorage.controller.properties.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {
    private static final String DIRECTORY_ERROR_MESSAGE = "Directory cannot bet created at path: %s.";
    private static final String FILE_ERROR_MESSAGE = "Could not store file %s. Please try again!";
    private static final String TWO_POINTS = "..";
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) throws IOException {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new IOException(String.format(DIRECTORY_ERROR_MESSAGE, this.fileStorageLocation), ex);
        }
    }

    public String storeFile(final MultipartFile file) throws InvalidSequence, IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (fileName.contains(TWO_POINTS)) {
            throw new InvalidSequence(TWO_POINTS);
        }
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new IOException(String.format(FILE_ERROR_MESSAGE, fileName), ex);
        }
    }

    public Resource loadFileAsResource(String fileName) throws FileNotFoundException, MalformedURLException {
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        Resource resource;
        resource = new UrlResource(filePath.toUri());
        if (!resource.exists()) {
            throw new FileNotFoundException(filePath.toString());
        }
        return resource;
    }
}
