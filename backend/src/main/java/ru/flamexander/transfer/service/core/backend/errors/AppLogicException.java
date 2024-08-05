package ru.flamexander.transfer.service.core.backend.errors;

import lombok.Getter;

@Getter
public class AppLogicException extends RuntimeException {
    private final String code;

    public AppLogicException(String code, String message) {
        super(message);
        this.code = code;
    }
}
