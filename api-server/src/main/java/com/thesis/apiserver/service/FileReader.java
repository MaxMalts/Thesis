package com.thesis.apiserver.service;

import com.thesis.apiserver.configuration.settings.GeneralSettings;
import com.thesis.apiserver.error.BusinessException;
import com.thesis.apiserver.persistence.FileSystemAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;

@Component
@RequiredArgsConstructor
public class FileReader {

    private final GeneralSettings generalSettings;
    private final FileSystemAdapter fileSystemAdapter;

    public FileSystemResource getFileSystemResource(String fileId) {
        var filePath = generalSettings.getFilesDirectory().resolve(fileId);

        if (!Files.exists(filePath)) {  // if disappeared after this check, exception will be thrown while accessing via FileSystemResource
            throw new BusinessException("File not found: " + filePath.toString(), "requested file was not found");
        }

        return fileSystemAdapter.getFileSystemResource(filePath);
    }
}
