package org.example.services;

import ru.flamexander.transfer.service.core.api.dtos.LimitDtoRequest;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoResponse;

public interface LimitService {
    LimitDtoResponse getLimit(LimitDtoRequest request);

    void returnLimit(LimitDtoRequest request);
}
