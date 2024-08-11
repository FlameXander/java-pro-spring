package ru.flamexander.transfer.service.core.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoRequest;
import ru.flamexander.transfer.service.core.backend.integrations.LimitsServiceIntegration;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LimitService {
    private final LimitsServiceIntegration limitsServiceIntegration;

    public boolean hasLimit(Long clientId, BigDecimal amount){
        LimitDtoRequest limitDtoRequest = new LimitDtoRequest(clientId, amount);
        return limitsServiceIntegration.getLimit(limitDtoRequest).isHasLimit();
    }

    public void returnLimit(Long clientId, BigDecimal amount){
        LimitDtoRequest limitDtoRequest = new LimitDtoRequest(clientId, amount);
        limitsServiceIntegration.returnLimit(limitDtoRequest);
    }
}
