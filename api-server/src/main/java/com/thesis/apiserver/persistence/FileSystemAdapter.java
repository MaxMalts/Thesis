package com.thesis.apiserver.persistence;

import com.thesis.apiserver.error.InternalException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class FileSystemAdapter {

    public void save(MultipartFile file, Path path) {
        try {
            file.transferTo(path);
        } catch (IOException ex) {
            throw new InternalException("Failed to save the file", ex);
        }
    }
}
