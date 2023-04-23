package com.thesis.apiserver.service;

import com.thesis.apiserver.configuration.settings.TextRecognizerSettings;
import com.thesis.apiserver.error.BusinessException;
import com.thesis.apiserver.error.InternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TextRecognizer {

    private final FileSystemAdapter fileSystemAdapter;
    private final TextRecognizerSettings textRecognizerSettings;

    public String recognizeText(MultipartFile image) {
        var filePath = textRecognizerSettings.getImagesDirectory().resolve(UUID.randomUUID().toString());
        fileSystemAdapter.save(image, filePath);

        return recognizeText(filePath);
    }

    public String recognizeText(Path imageFile) {
        Process recognizerProcess;
        try {
            recognizerProcess = new ProcessBuilder(textRecognizerSettings.getCommand(), imageFile.toString()).start();
        } catch (Exception ex) {
            throw new InternalException("Unable to start the text recognizer process", ex);
        }

        var exitCode = waitForProcess(recognizerProcess);
        if (exitCode != 0) {
            throw new InternalException("Text recognition process failed", "Error while recognizing text");
        }

        String result;
        try {
            result = new String(recognizerProcess.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new InternalException("Unable to read the output stream of text recognizer process", ex, "Error while recognizing text");
        }

        return result;
    }

    private int waitForProcess(Process process) {
        int exitCode;

        try {
            if (textRecognizerSettings.getRecognitionTimeoutMs() > 0) {
                boolean finished;
                finished = process.waitFor(textRecognizerSettings.getRecognitionTimeoutMs(), TimeUnit.MILLISECONDS);

                if (finished) {
                    exitCode = process.exitValue();
                } else {
                    throw new BusinessException("Recognition timeout", "You reached timeout limit");
                }

            } else {
                exitCode = process.waitFor();
            }

        } catch (InterruptedException ex) {
            throw new InternalException("Text recognition interrupted", ex);
        }

        return exitCode;
    }
}
