package ru.flamexander.transfer.service.limits.backend.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.flamexander.transfer.service.limits.backend.dtos.LimitRequest;
import ru.flamexander.transfer.service.limits.backend.dtos.LimitResponse;
import ru.flamexander.transfer.service.limits.backend.services.LimitsService;

@RestController
@RequestMapping("/api/v1/limits")
@RequiredArgsConstructor
public class LimitsController {

    private final LimitsService limitsService;

    @PostMapping("/deduct")
    public ResponseEntity<LimitResponse> deductLimit(@RequestBody LimitRequest request) {
        LimitResponse response = limitsService.deductLimit(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rollback")
    public ResponseEntity<LimitResponse> rollbackLimit(@RequestBody LimitRequest request) {
        LimitResponse response = limitsService.rollbackLimit(request);
        return ResponseEntity.ok(response);
    }
}
