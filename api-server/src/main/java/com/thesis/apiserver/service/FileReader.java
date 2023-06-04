package com.thesis.apiserver.service;

import com.thesis.apiserver.configuration.settings.GeneralSettings;
import com.thesis.apiserver.persistence.FileSystemAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileReader {

    private final GeneralSettings generalSettings;
    private final FileSystemAdapter fileSystemAdapter;

    public byte[] readFile(String fileId) {
        var filePath = generalSettings.getFilesDirectory().resolve(fileId);
        return fileSystemAdapter.read(filePath);
    }
}
