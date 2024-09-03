package ru.flamexander.transfer.service.core.backend.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.flamexander.transfer.service.core.backend.dtos.LimitsInfoRequestDto;
import ru.flamexander.transfer.service.core.backend.dtos.LimitsInfoResponseDto;
import ru.flamexander.transfer.service.core.backend.errors.AppLogicException;

@Component
@RequiredArgsConstructor
public class LimitsInfoServiceIntegration {
    private final RestTemplate restTemplate;

    @Value("${integrations.limits-info-service.url}")
    private String limitsServiceUrl;

    public LimitsInfoResponseDto deductClientLimit(LimitsInfoRequestDto request) {
        try {
            String url = limitsServiceUrl + "/deduct";
            return restTemplate.postForObject(url, request, LimitsInfoResponseDto.class);
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException(e, request.getUserId(), "deduct");
            throw e;
        }
    }

    public LimitsInfoResponseDto rollbackClientLimit(LimitsInfoRequestDto request) {
        try {
            String url = limitsServiceUrl + "/rollback";
            return restTemplate.postForObject(url, request, LimitsInfoResponseDto.class);
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException(e, request.getUserId(), "rollback");
            throw e;
        }
    }

    private void handleHttpClientErrorException(HttpClientErrorException e, Long userId, String operation) {
        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new AppLogicException("LIMIT_NOT_FOUND", String.format("Limit not found for userId = %d during %s operation", userId, operation));
        } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new AppLogicException("BAD_REQUEST", String.format("Bad request for userId = %d during %s operation", userId, operation));
        } else if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            throw new AppLogicException("INTERNAL_SERVER_ERROR", String.format("Internal server error during %s operation for userId = %d", operation, userId));
        }
    }
}
