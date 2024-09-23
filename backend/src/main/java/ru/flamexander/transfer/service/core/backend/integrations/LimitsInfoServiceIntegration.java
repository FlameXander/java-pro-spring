package ru.flamexander.transfer.service.core.backend.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.flamexander.transfer.service.core.backend.dtos.LimitsInfoRequestDto;
import ru.flamexander.transfer.service.core.backend.dtos.LimitsInfoResponseDto;
import ru.flamexander.transfer.service.core.backend.errors.AppLogicException;

@Component
@RequiredArgsConstructor
public class LimitsInfoServiceIntegration {
    private final WebClient webClient;

    @Value("${integrations.limits-info-service.url}")
    private String limitsServiceUrl;

    public Mono<LimitsInfoResponseDto> deductClientLimit(LimitsInfoRequestDto request) {
        String url = limitsServiceUrl + "/deduct";
        return webClient.post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LimitsInfoResponseDto.class)
                .onErrorResume(e -> handleException(e, request.getUserId(), "deduct"));
    }

    public Mono<LimitsInfoResponseDto> rollbackClientLimit(LimitsInfoRequestDto request) {
        String url = limitsServiceUrl + "/rollback";
        return webClient.post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LimitsInfoResponseDto.class)
                .onErrorResume(e -> handleException(e, request.getUserId(), "rollback"));
    }

    private Mono<LimitsInfoResponseDto> handleException(Throwable e, Long userId, String operation) {
        if (e instanceof WebClientResponseException ex) {
            HttpStatusCode status = ex.getStatusCode();
            if (status == HttpStatus.NOT_FOUND) {
                throw new AppLogicException("LIMIT_NOT_FOUND", String.format("Limit not found for userId = %d during %s operation", userId, operation));
            } else if (status == HttpStatus.BAD_REQUEST) {
                throw new AppLogicException("BAD_REQUEST", String.format("Bad request for userId = %d during %s operation", userId, operation));
            } else if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
                throw new AppLogicException("INTERNAL_SERVER_ERROR", String.format("Internal server error during %s operation for userId = %d", operation, userId));
            }
        }
        return Mono.error(e);
    }
}
