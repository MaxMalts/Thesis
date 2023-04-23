package com.thesis.apiserver.configuration;

import com.thesis.apiserver.configuration.settings.ErrorSettings;
import com.thesis.apiserver.dto.rest.ErrorResponse;
import com.thesis.apiserver.error.InternalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorSettings errorSettings;

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<Object> handleInternalException(InternalException ex, WebRequest request) {
        log.error("Internal exception occured", ex);

        var errorResponse = new ErrorResponse(errorSettings.getInternalError().getStatusCode(),
                                              ex.getMessage(),
                                              errorSettings.getInternalError().getDefaultUserMessage());

        return handleExceptionInternal(ex,
                                       errorResponse,
                                       new HttpHeaders(),
                                       HttpStatus.valueOf(errorSettings.getInternalError().getStatusCode()),
                                       request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnknownException(Exception ex, WebRequest request) {
        log.error("Unknown exception occured", ex);

        var errorResponse = new ErrorResponse(errorSettings.getUnknownError().getStatusCode(),
                                              ex.getMessage(),
                                              errorSettings.getUnknownError().getDefaultUserMessage());
        return handleExceptionInternal(ex,
                                       errorResponse,
                                       new HttpHeaders(),
                                       HttpStatusCode.valueOf(errorSettings.getUnknownError().getStatusCode()),
                                       request);
    }
}
