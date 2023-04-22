package com.thesis.apiserver.service;

import com.thesis.apiserver.error.InternalException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class FileSystemAdapter {

    public void save(MultipartFile file, Path path) {
        try {
            file.transferTo(path);
        } catch (IOException ex) {
            throw new InternalException("Failed to save the file", ex);
        }
    }
}
