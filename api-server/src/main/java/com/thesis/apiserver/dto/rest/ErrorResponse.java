package com.thesis.apiserver.dto.rest;

public record ErrorResponse(int statusCode, String message, String userMessage) {}
