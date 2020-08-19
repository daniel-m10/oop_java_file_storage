package com.jalasoft.filestorage.controller;

import com.jalasoft.filestorage.controller.exceptions.FileNotFoundException;
import com.jalasoft.filestorage.controller.exceptions.InvalidSequence;
import com.jalasoft.filestorage.controller.response.FileUploadErrorResponse;
import com.jalasoft.filestorage.controller.response.FileUploadOkResponse;
import com.jalasoft.filestorage.controller.response.Response;
import com.jalasoft.filestorage.controller.service.FileStorageService;
import com.jalasoft.filestorage.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public ResponseEntity<Response<Integer>> uploadFile(@RequestParam("file") final MultipartFile file) {
        String fileName;
        try {
            fileName = fileStorageService.storeFile(file);
        } catch (InvalidSequence | IOException e) {
            return ResponseEntity.badRequest()
                    .body(new FileUploadErrorResponse<>(HttpServletResponse.SC_BAD_REQUEST, e.getMessage()));
        }
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/downloadFile/")
                .path(Objects.requireNonNull(fileName))
                .toUriString();
        UploadedFile uploadedFile = new UploadedFile(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        return ResponseEntity.ok().body(new FileUploadOkResponse<>(HttpServletResponse.SC_OK, uploadedFile));
    }

    @PostMapping("/uploadMultipleFiles")
    public List<ResponseEntity<Response<Integer>>> uploadFiles(@RequestParam("files") final MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable final String fileName,
                                                 final HttpServletRequest request)
            throws FileNotFoundException, MalformedURLException {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
