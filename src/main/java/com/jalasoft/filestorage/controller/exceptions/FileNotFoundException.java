package com.jalasoft.filestorage.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FileNotFoundException extends Exception {
    private static final String FILE_NOT_FOUND_ERROR = "File not found at path: %s.";
    public FileNotFoundException(final String path) {
        super(String.format(FILE_NOT_FOUND_ERROR, path));
    }
}
