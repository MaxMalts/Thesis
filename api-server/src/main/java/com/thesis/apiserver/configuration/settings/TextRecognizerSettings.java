package com.thesis.apiserver.configuration.settings;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Path;

@Getter
@Setter
@Component
@ConfigurationProperties("text-recognizer")
@Validated
public class TextRecognizerSettings {

    @NotEmpty
    private String command;
    @NotNull
    private Path imagesDirectory;
    private int recognitionTimeoutMs = 20000;

    public void setImagesDirectory(String imagesDirectory) {
        this.imagesDirectory = Path.of(imagesDirectory);
    }

    public void setImagesDirectory(Path imagesDirectory) {
        this.imagesDirectory = imagesDirectory;
    }
}
