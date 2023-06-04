package com.thesis.apiserver.controller;

import com.thesis.apiserver.dto.rest.RecognizeTextResponse;
import com.thesis.apiserver.helper.FilesSaveHelper;
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
    private final FilesSaveHelper filesSaveHelper;

    @PostMapping(value = "recognize-text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecognizeTextResponse> recognizeText(@RequestPart("file") MultipartFile imageFile) {
        var text = textRecognizer.recognizeText(imageFile);
        var savedFilesNames = filesSaveHelper.saveToFiles(text);

        return ResponseEntity.ok(new RecognizeTextResponse(text,
                                                           savedFilesNames.pdfFileName(),
                                                           savedFilesNames.docxFileName(),
                                                           savedFilesNames.txtFileName()));
    }
}
