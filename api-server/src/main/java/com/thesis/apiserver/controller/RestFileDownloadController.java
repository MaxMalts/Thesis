package com.thesis.apiserver.controller;

import com.thesis.apiserver.service.FileReader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestFileDownloadController {

    private final FileReader fileReader;

    @GetMapping(value = "download-file/{fileId}", produces = "application/octet-stream")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) {
        return ResponseEntity.ok(fileReader.readFile(fileId));
    }
}
