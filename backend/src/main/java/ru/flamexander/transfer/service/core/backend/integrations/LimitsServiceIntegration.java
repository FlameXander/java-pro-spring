package ru.flamexander.transfer.service.core.backend.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoRequest;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoResponse;

@Component
@RequiredArgsConstructor
public class LimitsServiceIntegration {
    private final static String checkLimitUrl = "get";
    private final static String rollbackLimitUrl = "rollback";
    private final RestTemplate restTemplate;

    @Value("${integrations.limits-service.url}")
    private String limitServiceUrl;

    public LimitDtoResponse getLimit(LimitDtoRequest request, Long clientId) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.set("clientId", clientId.toString());

        HttpEntity<LimitDtoRequest> entity = new HttpEntity<LimitDtoRequest>(request, headers);

        ResponseEntity<LimitDtoResponse> response = restTemplate.exchange(
                limitServiceUrl + checkLimitUrl,
                HttpMethod.POST,
                entity,
                LimitDtoResponse.class
        );

        return response.getBody();
    }

    public void rollbackLimit(LimitDtoRequest request, Long clientId){
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.set("clientId", clientId.toString());

        HttpEntity<LimitDtoRequest> entity = new HttpEntity<LimitDtoRequest>(request, headers);

        restTemplate.exchange(
                limitServiceUrl + rollbackLimitUrl,
                HttpMethod.POST,
                entity,
                LimitDtoResponse.class
        );
    }
}
