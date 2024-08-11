package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.entities.Limit;
import org.example.repository.LimitRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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
    public LimitDtoResponse getLimit(LimitDtoRequest request) {
        var limit = limitRepository.findByClientId(request.getClientId()).orElse(new Limit(request.getClientId(), new BigDecimal(defaultLimit)));

        if (limit.getLimit().compareTo(request.getMoney()) >= 0) {
            limit.setLimit(limit.getLimit().subtract(request.getMoney()));
            limitRepository.save(limit);
            return new LimitDtoResponse(true);
        } else {
            limitRepository.save(limit);
            return new LimitDtoResponse(false);
        }
    }

    @Override
    public void returnLimit(LimitDtoRequest request) {
        var limit = limitRepository.findByClientId(request.getClientId()).orElse(new Limit(request.getClientId(), new BigDecimal(defaultLimit)));

        limit.setLimit(limit.getLimit().add(request.getMoney()));
        limitRepository.save(limit);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void resetLimit() {
        var allLimits = limitRepository.findAll();
        for (var limit : allLimits) {
            limit.setLimit(limit.getLimitMax());
            limitRepository.save(limit);
        }
    }
}
