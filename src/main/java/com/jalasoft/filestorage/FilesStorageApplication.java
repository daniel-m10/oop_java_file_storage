package com.jalasoft.filestorage;

import com.jalasoft.filestorage.controller.properties.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class FilesStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilesStorageApplication.class, args);
    }
}
