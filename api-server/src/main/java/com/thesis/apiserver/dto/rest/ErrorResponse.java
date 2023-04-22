package com.thesis.apiserver.dto.rest;

public record ErrorResponse(int errorCode, String message, String userMessage) {}
