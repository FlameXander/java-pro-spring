package ru.flamexander.transfer.service.core.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.flamexander.transfer.service.core.api.dtos.ExecuteTransferDtoRequest;
import ru.flamexander.transfer.service.core.api.dtos.ExecuteTransferDtoResult;
import ru.flamexander.transfer.service.core.api.dtos.TransferDto;
import ru.flamexander.transfer.service.core.api.dtos.TransferPageDto;
import ru.flamexander.transfer.service.core.backend.entities.Transfer;
import ru.flamexander.transfer.service.core.backend.services.TransferService;

import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
@Tag(name = "Переводы клиентов", description = "Методы переводов между счетами клиентов")
public class TransfersController {
    private final TransferService transferService;
    private final Function<Transfer, TransferDto> entityToDto = transfer ->
            new TransferDto(transfer.getSenderAccountNumber(), transfer.getRecipientAccountNumber(),
                    transfer.getTransferAmount(), transfer.getStatus().toString(), transfer.getUpdatedAt());

    @Operation(summary = "Перевод")
    @PostMapping("/execute")
    public ExecuteTransferDtoResult executeTransfer(@RequestBody ExecuteTransferDtoRequest request) {
        return transferService.transfer(request);
    }
    @Operation(summary = "Результат")
    @GetMapping
    public TransferPageDto getAllTransfers(@RequestHeader Long clientId) {
        return new TransferPageDto(transferService.getAllTransfers(clientId).stream().map(entityToDto).collect(Collectors.toList()));
    }
}
