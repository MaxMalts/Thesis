package com.thesis.apiserver.controller;

import com.thesis.apiserver.dto.rest.RecognizeTextResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class RestRecognitionController {

    @PostMapping(value = "/recognize-text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecognizeTextResponse> recognizeText(@RequestPart("file") MultipartFile imageFile) {
        return ResponseEntity.ok(new RecognizeTextResponse("Hello world!"));
    }
}
