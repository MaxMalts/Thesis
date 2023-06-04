package com.thesis.apiserver.persistence;

import com.thesis.apiserver.error.InternalException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
public class FileSystemAdapter {

    public void save(MultipartFile file, Path path) {
        try {
            Files.createDirectories(path.getParent());
            file.transferTo(path);
        } catch (IOException e) {
            throw new InternalException("Failed to save the file", e);
        }
    }

    public void save(byte[] data, Path path) {
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new InternalException("Failed to create directories for the file", e);
        }

        try (var output = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW)) {
            output.write(data);
        } catch (IOException e) {
            throw new InternalException("Failed to write to file", e);
        }
    }

    public byte[] read(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new InternalException("Failed to read the file", e);
        }
    }

    public FileSystemResource getFileSystemResource(Path path) {
        return new FileSystemResource(path);
    }
}
