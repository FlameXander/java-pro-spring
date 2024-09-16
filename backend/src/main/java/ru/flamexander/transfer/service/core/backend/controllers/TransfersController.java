package ru.flamexander.transfer.service.core.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.flamexander.transfer.service.core.api.dtos.ExecuteTransferDtoRequest;
import ru.flamexander.transfer.service.core.backend.errors.InsufficientLimitException;
import ru.flamexander.transfer.service.core.backend.services.TransfersService;
import ru.flamexander.transfer.service.core.backend.services.LimitsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
public class TransfersController {
    private final TransfersService transfersService;
    private final LimitsService limitsService;

    @PostMapping("/execute")
    public void executeTransfer(
            @RequestHeader Long clientId,
            @RequestBody ExecuteTransferDtoRequest executeTransferDtoRequest
    ) {
        boolean limitDeducted = limitsService.deductLimit(clientId, executeTransferDtoRequest.getTransferSum().longValue());

        if (!limitDeducted) {
            throw new InsufficientLimitException("Недостаточно лимита для выполнения перевода.");
        }

        try {
            transfersService.execute(clientId, executeTransferDtoRequest);
        } catch (Exception e) {
            limitsService.rollbackLimit(clientId, executeTransferDtoRequest.getTransferSum().longValue());
            throw e;
        }
    }
}
