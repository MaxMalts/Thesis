package com.thesis.apiserver.error;

public class InternalException extends BaseException {

    public InternalException() {
        super();
    }

    public InternalException(String message) {
        super(message);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalException(Throwable cause) {
        super(cause);
    }

    public InternalException(String message, String userMessage) {
        super(message, userMessage);
    }

    public InternalException(String message, Throwable cause, String userMessage) {
        super(message, cause, userMessage);
    }

    public InternalException(Throwable cause, String userMessage) {
        super(cause, userMessage);
    }
}
