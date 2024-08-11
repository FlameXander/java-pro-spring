package ru.flamexander.transfer.service.core.backend.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoRequest;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoResponse;

@Component
@RequiredArgsConstructor
public class LimitsServiceIntegration {
    private final RestTemplate restTemplate;

    @Value("${integrations.limits-service.check-limit.url}")
    private String checkLimitUrl;
    @Value("${integrations.limits-service.return-limit.url}")
    private String returnLimitUrl;

    public LimitDtoResponse getLimit(LimitDtoRequest request) {
        LimitDtoResponse receiverInfo = restTemplate.postForObject(checkLimitUrl, request, LimitDtoResponse.class);
            return receiverInfo;
    }

    public void returnLimit(LimitDtoRequest request){
        restTemplate.postForObject(returnLimitUrl, request, LimitDtoResponse.class);
    }
}
