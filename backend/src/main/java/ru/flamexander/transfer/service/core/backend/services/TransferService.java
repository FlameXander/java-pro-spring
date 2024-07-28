package ru.flamexander.transfer.service.core.backend.services;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.flamexander.transfer.service.core.api.dtos.ExecuteTransferDtoRequest;
import ru.flamexander.transfer.service.core.api.dtos.ExecuteTransferDtoResult;
import ru.flamexander.transfer.service.core.api.dtos.ReportTransferDtoResult;
import ru.flamexander.transfer.service.core.api.dtos.ReportTransferPageDtoResult;
import ru.flamexander.transfer.service.core.backend.controllers.TransfersController;
import ru.flamexander.transfer.service.core.backend.entities.Account;
import ru.flamexander.transfer.service.core.backend.entities.TransferDocument;
import ru.flamexander.transfer.service.core.backend.errors.AppLogicException;
import ru.flamexander.transfer.service.core.backend.repositories.AccountsRepository;
import ru.flamexander.transfer.service.core.backend.repositories.TransferDocumentRepository;
import ru.flamexander.transfer.service.core.backend.validators.ExecuteTransferValidator;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);
    private final AccountsService accountsService;
    private final ExecuteTransferValidator executeTransferValidator;
    private final AccountsRepository accountsRepository;
    private final TransferDocumentRepository transferDocumentRepository;

    public TransferDocument makeTransfer(ExecuteTransferDtoRequest request) {
        logger.info("TransferService1 request:  {}", request);
        executeTransferValidator.validate(request);
        logger.info("TransferService2 request:  {}", request);
        TransferDocument transferDocument = new TransferDocument(request.getId(),
                request.getSenderId(),
                request.getSendAccount(),
                request.getReceiverId(),
                request.getReceivAccount(),
                request.getAmount());
        transferDocument.setDocumentStatus(DocumentStatus.IN_PROGRESS);
        transferDocument.setUpdateDateTime(ZonedDateTime.now(ZoneOffset.UTC));
        logger.info("TransferService3 transferDocument:  {}", transferDocument);
        transfer(transferDocument); // Вся логика..

        transferDocument.setDocumentStatus(DocumentStatus.COMPLETED);
        transferDocument.setUpdateDateTime(ZonedDateTime.now(ZoneOffset.UTC));
        logger.info("TransferService4 transferDocument:  {}", transferDocument);
        transferDocument = transferDocumentRepository.save(transferDocument);
        return transferDocument;
    }

    @Transactional
    public void transfer(TransferDocument transferDocument) {
// TODO как прописывать в БД transferDocument.setDocumentStatus(DocumentStatus.ERROR); при исключении?
        Account source = accountsService.getAccountByAccountNumber(transferDocument.getSendAccount(), transferDocument.getSenderId()).orElseThrow(() -> new AppLogicException("TRANSFER_SOURCE_ACCOUNT_NOT_FOUND", "Перевод невозможен поскольку не существует счет отправителя"));
        logger.info("TransferService5 source:  {}", source);
        Account target = accountsService.getAccountByAccountNumber(transferDocument.getReceivAccount(), transferDocument.getReceiverId()).orElseThrow(() -> new AppLogicException("TRANSFER_TARGET_ACCOUNT_NOT_FOUND", "Перевод невозможен поскольку не существует счет получателя"));
        logger.info("TransferService6 target:  {}", target);
        source.setBalance(source.getBalance().add(transferDocument.getAmount().negate()));
        source = accountsRepository.save(source);
        logger.info("TransferService7 source:  {}", source);
        target.setBalance(target.getBalance().add(transferDocument.getAmount()));
        target = accountsRepository.save(target);
        logger.info("TransferService8 target:  {}", target);
    }

    public ReportTransferPageDtoResult reportTransfers(Long clientId) {
        logger.info("reportTransfers1 clientId:  {}", clientId);

        ReportTransferPageDtoResult reportTransferPageDtoResult = new ReportTransferPageDtoResult(new ArrayList<ReportTransferDtoResult>());

        List<TransferDocument> transferDocuments = transferDocumentRepository.findAllBySenderId(clientId); // исходящие
        logger.info("reportTransfers2 исходящие transferDocuments:  {}", transferDocuments);
        reportTransferPageDtoResult.getItems().addAll(makeReportDtoFromDocument(transferDocuments, false));

        transferDocuments = transferDocumentRepository.findAllByReceiverId(clientId); // входящие
        logger.info("reportTransfers3 входящие transferDocuments:  {}", transferDocuments);
        reportTransferPageDtoResult.getItems().addAll(makeReportDtoFromDocument(transferDocuments, true));
        logger.info("reportTransfers4 reportTransferPageDtoResult:  {}", reportTransferPageDtoResult);
        return reportTransferPageDtoResult;
    }

    private List<ReportTransferDtoResult> makeReportDtoFromDocument(List<TransferDocument> transferDocuments, Boolean direct) {
        List<ReportTransferDtoResult> reportTransferDtoResults = new ArrayList<>();
        ReportTransferDtoResult reportTransferDtoResult;
        for (TransferDocument transferDocument : transferDocuments) {
            reportTransferDtoResult = new ReportTransferDtoResult(
                    transferDocument.getSendAccount(),
                    transferDocument.getReceiverId(),
                    transferDocument.getReceivAccount(),
                    transferDocument.getAmount(),
                    transferDocument.getDocumentStatus().toString(),
                    transferDocument.getUpdateDateTime());
            reportTransferDtoResult.setDirection(direct);
            if (direct) {
                reportTransferDtoResult.setClientAccount(transferDocument.getReceivAccount());
                reportTransferDtoResult.setContractorId(transferDocument.getSenderId());
                reportTransferDtoResult.setContractorAccount(transferDocument.getSendAccount());
            } else {
                reportTransferDtoResult.setClientAccount(transferDocument.getSendAccount());
                reportTransferDtoResult.setContractorId(transferDocument.getReceiverId());
                reportTransferDtoResult.setContractorAccount(transferDocument.getReceivAccount());
            }
            reportTransferDtoResults.add(reportTransferDtoResult);
        }
        return reportTransferDtoResults;
    }
}

