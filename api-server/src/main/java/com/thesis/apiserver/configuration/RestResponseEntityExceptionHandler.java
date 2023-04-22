package com.thesis.apiserver.configuration;

import com.thesis.apiserver.configuration.settings.ErrorSettings;
import com.thesis.apiserver.dto.rest.ErrorResponse;
import com.thesis.apiserver.error.InternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RequiredArgsConstructor
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorSettings errorSettings;

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<Object> handleInternalException(InternalException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                                       "Test body",
                                       new HttpHeaders(),
                                       HttpStatus.valueOf(errorSettings.getInternalError().getStatusCode()),
                                       request);
    }

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<Object> handleUnknownException(Throwable ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                     ex.getMessage(),
                                                     errorSettings.getUnknownError().getDefaultUserMessage()));
    }
}
