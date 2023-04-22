package com.thesis.apiserver.controller;

import com.thesis.apiserver.dto.rest.RecognizeTextResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class RestRecognitionController {

    @PostMapping(value = "recognize-text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecognizeTextResponse> recognizeText(@RequestPart("file") MultipartFile imageFile) {
        var recognizerProcess = new ProcessBuilder("python");

        return ResponseEntity.ok(new RecognizeTextResponse("Hello world!"));
    }
}
