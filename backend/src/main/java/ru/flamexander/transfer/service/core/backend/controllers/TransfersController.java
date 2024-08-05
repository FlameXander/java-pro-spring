package ru.flamexander.transfer.service.core.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.flamexander.transfer.service.core.api.dtos.ExecuteTransferDtoRequest;
import ru.flamexander.transfer.service.core.api.dtos.ExecuteTransferDtoResult;
import ru.flamexander.transfer.service.core.api.dtos.ReportTransferDtoResult;
import ru.flamexander.transfer.service.core.api.dtos.ReportTransferPageDtoResult;
import ru.flamexander.transfer.service.core.backend.entities.Account;
import ru.flamexander.transfer.service.core.backend.entities.TransferDocument;
import ru.flamexander.transfer.service.core.backend.errors.AppLogicException;
import ru.flamexander.transfer.service.core.backend.services.AccountsService;
import ru.flamexander.transfer.service.core.backend.services.DocumentStatus;
import ru.flamexander.transfer.service.core.backend.services.TransferService;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
@Tag(name = "Переводы клиентов", description = "Методы работы с переводами клиентов")
public class TransfersController {
    private static final Logger logger = LoggerFactory.getLogger(TransfersController.class);

    private final TransferService transferService;
    private final AccountsService accountsService;

    private Function<TransferDocument, ExecuteTransferDtoResult> documentToDto = transferDocument -> new ExecuteTransferDtoResult(transferDocument.getRequestId(),
            transferDocument.getDocumentStatus().toString(),
            transferDocument.getUpdateDateTime());

    @Operation(summary = "Перевод")
    @PostMapping("/execute")
    public ResponseEntity<ExecuteTransferDtoResult> executeTransfer(@RequestBody ExecuteTransferDtoRequest executeTransferDtoRequest) {
        logger.info("TransfersController0 ----------------------- Nowy Zapros:  {} ----------------------\n\n", ZonedDateTime.now(ZoneOffset.UTC));
        Account source = accountsService.getAccountByAccountNumber(executeTransferDtoRequest.getSendAccount(), executeTransferDtoRequest.getSenderId()).orElseThrow(() -> new AppLogicException("TRANSFER_SOURCE_ACCOUNT_NOT_FOUND", "Перевод невозможен поскольку не существует счет отправителя"));
        logger.info("TransfersController00 source:  {}", source);
        //   Account target = accountsService.getAccountById(1L, targetAccountId).orElseThrow(() -> new AppLogicException("TRANSFER_TARGET_ACCOUNT_NOT_FOUND", "Перевод невозможен поскольку не существует счет получателяч"));
        Account target = accountsService.getAccountByAccountNumber(executeTransferDtoRequest.getReceivAccount(), executeTransferDtoRequest.getReceiverId()).orElseThrow(() -> new AppLogicException("TRANSFER_TARGET_ACCOUNT_NOT_FOUND", "Перевод невозможен поскольку не существует счет получателяч"));
        logger.info("TransfersController000 target:  {}", target);
        logger.info("TransfersController1 ExecuteTransferDtoRequest:  {}", executeTransferDtoRequest);
        TransferDocument transferDocument = transferService.makeTransfer(executeTransferDtoRequest);
        logger.info("TransfersController2 transferDocument:  {}", transferDocument);
        logger.info("TransfersController3 ExecuteTransferDtoResult:  {}", documentToDto.apply(transferDocument));
        return ResponseEntity.status(HttpStatus.CREATED).body(documentToDto.apply(transferDocument));
    }

    @Operation(summary = "отчет")
    @GetMapping("/report")
    public ResponseEntity<ReportTransferPageDtoResult> reportTransfer(
            @RequestHeader @Parameter(description = "Идентификатор клиента для которого создается отчет") Long clientId) {
        logger.info("reportTransfer0 ----------------------- Nowy Raport:  {} ----------------------\n\n", ZonedDateTime.now(ZoneOffset.UTC));
        logger.info("reportTransfer1 clientId:  {}", clientId);
        ReportTransferPageDtoResult reportTransferPageDtoResult = transferService.reportTransfers(clientId);
        logger.info("reportTransfer2 reportTransferPageDtoResult:  {}", reportTransferPageDtoResult);

        return ResponseEntity.status(HttpStatus.CREATED).body(reportTransferPageDtoResult);
    }
}
