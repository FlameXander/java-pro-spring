package ru.flamexander.transfer.service.limits.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.flamexander.transfer.service.limits.backend.config.LimitsConfig;
import ru.flamexander.transfer.service.limits.backend.entities.Limit;
import ru.flamexander.transfer.service.limits.backend.repository.LimitsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LimitsResetService {

    private final LimitsRepository limitRepository;
    private final LimitsConfig limitsConfig;

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetAllLimits() {
        List<Limit> limits = limitRepository.findAll();
        limits.forEach(limit -> limit.setLimitAmount(limitsConfig.getDefaultLimit()));
        limitRepository.saveAll(limits);
    }
}
