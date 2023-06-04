package com.thesis.apiserver.helper;

import com.thesis.apiserver.configuration.settings.GeneralSettings;
import com.thesis.apiserver.dto.SavedFilesNames;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FilesSaveHelper {

    private final GeneralSettings generalSettings;
    private final TextFileConvertHelper textFileConvertHelper;

    public SavedFilesNames saveToFiles(String text) {

        var pdfPath = generalSettings.getFilesDirectory().resolve(UUID.randomUUID().toString() + ".pdf");
        textFileConvertHelper.writePdf(text, pdfPath);

        return new SavedFilesNames(pdfPath.getFileName().toString(), null, null);
    }
}
