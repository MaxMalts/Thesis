package com.thesis.apiserver.configuration.settings;

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
@Validated
@ConfigurationProperties("settings")
public class GeneralSettings {

    @NotNull
    private Path filesDirectory;
}
