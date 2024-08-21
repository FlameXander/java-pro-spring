package ru.flamexander.transfer.service.core.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.flamexander.transfer.service.core.backend.entities.Limit;
import ru.flamexander.transfer.service.core.backend.errors.ResourceNotFoundException;
import ru.flamexander.transfer.service.core.backend.repositories.LimitRepository;

@Service
public class LimitsServiceImpl implements LimitsService {

    private final LimitRepository limitRepository;

    @Value("${app.limits.default}")
    private Long defaultLimit;

    public LimitsServiceImpl(LimitRepository limitRepository) {
        this.limitRepository = limitRepository;
    }

    @Override
    public boolean deductLimit(Long userId, Long amount) {
        Limit limit = limitRepository.findByUserId(userId).orElseGet(() -> {
            Limit newLimit = new Limit(userId, defaultLimit);
            limitRepository.save(newLimit);
            return newLimit;
        });

        if (limit.getAmount() >= amount) {
            limit.setAmount(limit.getAmount() - amount);
            limitRepository.save(limit);
            return true;
        }

        return false;
    }

    @Override
    public void rollbackLimit(Long userId, Long amount) {
        Limit limit = limitRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Limit not found for user: " + userId));
        limit.setAmount(limit.getAmount() + amount);
        limitRepository.save(limit);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void resetLimits() {
        limitRepository.findAll().forEach(limit -> {
            limit.setAmount(defaultLimit);
            limitRepository.save(limit);
        });
    }
}
