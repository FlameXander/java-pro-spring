package ru.flamexander.transfer.service.limits.backend.services;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.flamexander.transfer.service.limits.backend.config.LimitsConfig;
import ru.flamexander.transfer.service.limits.backend.dtos.LimitRequest;
import ru.flamexander.transfer.service.limits.backend.dtos.LimitResponse;
import ru.flamexander.transfer.service.limits.backend.entities.Limit;
import ru.flamexander.transfer.service.limits.backend.errors.LimitServiceException;
import ru.flamexander.transfer.service.limits.backend.repository.LimitsRepository;

@Service
@RequiredArgsConstructor
public class LimitsService {

    private final LimitsRepository limitRepository;
    private final LimitsConfig limitsConfig;

    public LimitResponse deductLimit(LimitRequest request) {
        Limit limit = limitRepository.findByUserId(request.getUserId())
                .orElseGet(() -> createDefaultLimit(request.getUserId()));

        if (limit.getLimitAmount() < request.getAmount()) {
            throw new LimitServiceException("Insufficient limit");
        }

        limit.setLimitAmount(limit.getLimitAmount() - request.getAmount());
        limitRepository.save(limit);

        return new LimitResponse(limit.getUserId(), limit.getLimitAmount());
    }

    public LimitResponse rollbackLimit(LimitRequest request) {
        Limit limit = limitRepository.findByUserId(request.getUserId())
                .orElseGet(() -> createDefaultLimit(request.getUserId()));

        limit.setLimitAmount(limit.getLimitAmount() + request.getAmount());
        limitRepository.save(limit);

        return new LimitResponse(limit.getUserId(), limit.getLimitAmount());
    }

    private Limit createDefaultLimit(Long userId) {
        Limit limit = new Limit();
        limit.setUserId(userId);
        limit.setLimitAmount(limitsConfig.getDefaultLimit());
        return limitRepository.save(limit);
    }
}
