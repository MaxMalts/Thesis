package com.thesis.apiserver.error;

public class BusinessException extends BaseException {

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, String userMessage) {
        super(message, userMessage);
    }

    public BusinessException(String message, Throwable cause, String userMessage) {
        super(message, cause, userMessage);
    }

    public BusinessException(Throwable cause, String userMessage) {
        super(cause, userMessage);
    }
}
