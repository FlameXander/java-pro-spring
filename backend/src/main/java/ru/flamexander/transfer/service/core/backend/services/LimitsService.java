package ru.flamexander.transfer.service.core.backend.services;

public interface LimitsService {
    boolean deductLimit(Long userId, Long amount);
    void rollbackLimit(Long userId, Long amount);
    void resetLimits();
}
