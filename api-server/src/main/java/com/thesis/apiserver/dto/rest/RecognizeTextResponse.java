package com.thesis.apiserver.dto.rest;

public record RecognizeTextResponse(String text, String pdfId, String docxId, String txtId) {}
