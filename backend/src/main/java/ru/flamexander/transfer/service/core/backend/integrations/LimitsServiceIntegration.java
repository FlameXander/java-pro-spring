package ru.flamexander.transfer.service.core.backend.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoRequest;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoResponse;

@Component
@RequiredArgsConstructor
public class LimitsServiceIntegration {
    private final static String checkLimitUrl = "get";
    private final static String rollbackLimitUrl = "rollback";
    private final RestClient limitsClient;

    public LimitDtoResponse getLimit(LimitDtoRequest request, Long clientId) {
        return limitsClient.post()
                .uri("/{api}", checkLimitUrl)
                .header("clientId", clientId.toString())
                .retrieve()
                .body(LimitDtoResponse.class);
    }

    public void rollbackLimit(LimitDtoRequest request, Long clientId){
        limitsClient.post()
                .uri("/{api}", rollbackLimitUrl)
                .header("clientId", clientId.toString())
                .retrieve()
                .body(LimitDtoResponse.class);
    }
}
