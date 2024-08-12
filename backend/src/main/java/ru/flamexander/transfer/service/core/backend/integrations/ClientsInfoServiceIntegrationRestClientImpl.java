package ru.flamexander.transfer.service.core.backend.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import ru.flamexander.transfer.service.core.backend.dtos.ClientInfoResponseDto;
import ru.flamexander.transfer.service.core.backend.errors.AppLogicException;

@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(RestTemplate.class)
public class ClientsInfoServiceIntegrationRestClientImpl implements ClientsInfoServiceIntegration {
    private final RestClient clientsInfoClient;

    public ClientInfoResponseDto getClientInfo(Long id) {
        return clientsInfoClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.NOT_FOUND.value(), (request, response) -> {
                    throw new AppLogicException("RECEIVER_NOT_EXIST", "Получатель с id = " + id + " не существует");
                })
                .body(ClientInfoResponseDto.class);
    }
}
