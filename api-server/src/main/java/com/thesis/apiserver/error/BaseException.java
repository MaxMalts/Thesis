package com.thesis.apiserver.error;

import lombok.Getter;

public abstract class BaseException extends RuntimeException {

    @Getter
    private String userMessage;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }

    public BaseException(String message, Throwable cause, String userMessage) {
        super(message, cause);
        this.userMessage = userMessage;
    }

    public BaseException(Throwable cause, String userMessage) {
        super(cause);
        this.userMessage = userMessage;
    }
}
