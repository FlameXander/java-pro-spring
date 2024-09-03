package ru.flamexander.transfer.service.limits.backend.errors;

public class LimitServiceException extends RuntimeException {

    public LimitServiceException(String message) {
        super(message);
    }

    public LimitServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
