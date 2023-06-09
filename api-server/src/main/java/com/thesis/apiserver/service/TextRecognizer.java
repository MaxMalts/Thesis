package com.thesis.apiserver.service;

import com.thesis.apiserver.configuration.settings.TextRecognizerSettings;
import com.thesis.apiserver.dto.Area;
import com.thesis.apiserver.error.BusinessException;
import com.thesis.apiserver.error.InternalException;
import com.thesis.apiserver.persistence.FileSystemAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextRecognizer {

    private final FileSystemAdapter fileSystemAdapter;
    private final TextRecognizerSettings textRecognizerSettings;

    public String recognizeText(MultipartFile image, Area area) {
        var filePath = textRecognizerSettings.getImagesDirectory().resolve(UUID.randomUUID().toString());
        fileSystemAdapter.save(image, filePath);

        return recognizeText(filePath, area);
    }

    public String recognizeText(Path imageFile, Area area) {
        String[] command = new String[]{"bash", "-c", String.format("%s %s %d %d %d %d",
                                                                    textRecognizerSettings.getCommand(),
                                                                    imageFile.toString(),
                                                                    area.x1(),
                                                                    area.y1(),
                                                                    area.x2(),
                                                                    area.y2())};

        Process recognizerProcess;
        try {
            recognizerProcess = new ProcessBuilder(command).start();
        } catch (Exception e) {
            throw new InternalException("Unable to start the text recognizer process", e);
        }

        var exitCode = waitForProcess(recognizerProcess);
        if (exitCode != 0) {
            try {
                log.debug("Text recognition process returned non zero exit code. Error stream:\n" +
                          new String(recognizerProcess.getErrorStream().readAllBytes()));
            } catch (IOException ex) {
                log.warn("Unable to read stderr from text recognizer process", ex);
            }

            throw new InternalException("Text recognition process failed", "error occurred while recognizing text");
        }

        String result;
        try {
            result = new String(recognizerProcess.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new InternalException("Unable to read the output stream of text recognizer process",
                                        e,
                                        "error occurred while recognizing text");
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
                    process.destroyForcibly();
                    throw new BusinessException("Recognition timeout", "recognition timeout limit exceeded, try again");
                }

            } else {
                exitCode = process.waitFor();
            }

        } catch (InterruptedException e) {
            throw new InternalException("Text recognition interrupted", e);
        }

        return exitCode;
    }
}
