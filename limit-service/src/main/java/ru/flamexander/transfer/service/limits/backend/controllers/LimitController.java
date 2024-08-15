package ru.flamexander.transfer.service.limits.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoRequest;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoResponse;
import ru.flamexander.transfer.service.limits.backend.services.LimitService;

@RestController
@RequestMapping("/api/v1/limits")
@RequiredArgsConstructor
public class LimitController {
    private final LimitService limitService;

    @PostMapping("/get")
    public LimitDtoResponse getClientLimit(@RequestHeader Long clientId,
                                           @RequestBody LimitDtoRequest request){
        return limitService.getLimit(request, clientId);
    }

    @PostMapping("/rollback")
    public void rollbackClientLimit(@RequestHeader Long clientId,
                                    @RequestBody LimitDtoRequest request){
        limitService.returnLimit(request, clientId);
    }
}
