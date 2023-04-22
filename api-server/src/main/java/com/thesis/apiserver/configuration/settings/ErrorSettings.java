package com.thesis.apiserver.configuration.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("error")
public class ErrorSettings {

    private ErrorTypeSettings internalError = new ErrorTypeSettings(500, "server error");
    private ErrorTypeSettings businessError = new ErrorTypeSettings(422, "error");
    private ErrorTypeSettings unknownError = new ErrorTypeSettings(422, "unknown error");

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorTypeSettings {
        private int statusCode;
        private String defaultUserMessage;
    }
}
