package ru.bublinoid.limitservice.service;

import ru.flamexander.transfer.service.core.api.dtos.LimitDtoRequest;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoResponse;


public interface LimitService {
    LimitDtoResponse getLimit(LimitDtoRequest request, Long clientId);

    void returnLimit(LimitDtoRequest request, Long clientId);
}