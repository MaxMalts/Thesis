package com.thesis.apiserver.controller;

import com.thesis.apiserver.error.BusinessException;
import com.thesis.apiserver.service.FileReader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
        int dotInd = fileId.lastIndexOf('.');
        if (-1 == dotInd || fileId.endsWith(".")) {
            throw new BusinessException("Bad fileId received");
        }

        var headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment; filename=\"Recognized Text." + fileId.substring(dotInd + 1));
        headers.set("Cache-Control", "no-cache no-store");
        headers.set("Pragma", "no-cache");
        return ResponseEntity.ok().headers(headers).body(fileReader.readFile(fileId));
    }
}
