package ru.flamexander.transfer.service.core.backend.errors;

public class InsufficientLimitException extends AppLogicException {
    private static final String ERROR_CODE = "INSUFFICIENT_LIMIT";

    public InsufficientLimitException(String message) {
        super(ERROR_CODE, message);
    }
}
