package org.example.controllers;

import lombok.RequiredArgsConstructor;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoRequest;
import ru.flamexander.transfer.service.core.api.dtos.LimitDtoResponse;
import org.example.services.LimitService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/limits")
@RequiredArgsConstructor
public class LimitController {
    private final LimitService limitService;

    @PostMapping("/check")
    public LimitDtoResponse getClientLimit(@RequestBody LimitDtoRequest request){
        return limitService.getLimit(request);
    }

    @PostMapping("/return")
    public void returnClientLimit(@RequestBody LimitDtoRequest request){
        limitService.returnLimit(request);
    }
}
