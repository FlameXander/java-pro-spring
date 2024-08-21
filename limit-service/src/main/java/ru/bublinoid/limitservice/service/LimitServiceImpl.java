package ru.bublinoid.limitservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bublinoid.limitservice.repository.LimitRepository;
import ru.bublinoid.limitservice.entity.Limit;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoRequest;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoResponse;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LimitServiceImpl implements LimitService {
    @Value("${application.limits.default_limit}")
    private final int defaultLimit = 10000;
    private final LimitRepository limitRepository;

    @Override
    @Transactional
    public LimitDtoResponse getLimit(LimitDtoRequest request, Long clientId) {
        var limit = limitRepository.findByClientId(clientId).orElse(limitRepository.save(new Limit(clientId, new BigDecimal(defaultLimit))));

        var success = limit.getLimit().compareTo(request.getMoney()) >= 0;
        if (success) {
            limit.setLimit(limit.getLimit().subtract(request.getMoney()));
        }
        return new LimitDtoResponse(success);
    }

    @Override
    @Transactional
    public void returnLimit(LimitDtoRequest request, Long clientId) {
        var limitOptional = limitRepository.findByClientId(clientId);

        if (limitOptional.isPresent()){
            var limit = limitOptional.get();
            limit.setLimit(limit.getLimit().add(request.getMoney()));
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    protected void resetLimit() {
        limitRepository.resetAllLimits();
    }
}