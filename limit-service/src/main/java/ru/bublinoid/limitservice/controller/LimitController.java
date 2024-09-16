package ru.bublinoid.limitservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.bublinoid.limitservice.service.LimitService;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoRequest;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoResponse;


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