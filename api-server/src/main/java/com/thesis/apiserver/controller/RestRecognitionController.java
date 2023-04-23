package com.thesis.apiserver.controller;

import com.thesis.apiserver.dto.rest.RecognizeTextResponse;
import com.thesis.apiserver.service.TextRecognizer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestRecognitionController {

    private final TextRecognizer textRecognizer;

    @PostMapping(value = "recognize-text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecognizeTextResponse> recognizeText(@RequestPart("file") MultipartFile imageFile) {
        return ResponseEntity.ok(new RecognizeTextResponse(textRecognizer.recognizeText(imageFile)));
    }
}
