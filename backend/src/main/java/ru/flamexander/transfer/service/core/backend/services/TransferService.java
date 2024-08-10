package ru.flamexander.transfer.service.core.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.flamexander.transfer.service.core.api.dtos.ExecuteTransferDtoRequest;
import ru.flamexander.transfer.service.core.api.dtos.ExecuteTransferDtoResult;
import ru.flamexander.transfer.service.core.backend.entities.Account;
import ru.flamexander.transfer.service.core.backend.entities.Transfer;
import ru.flamexander.transfer.service.core.backend.errors.AppLogicException;
import ru.flamexander.transfer.service.core.backend.repositories.TransferRepository;
import ru.flamexander.transfer.service.core.backend.statuses.TransferStatus;
import ru.flamexander.transfer.service.core.backend.validators.ExecuteTransferValidator;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);
    private final AccountsService accountsService;
    private final ExecuteTransferValidator executeTransferValidator;
    private final TransferRepository transferRepository;

    @Transactional
    public ExecuteTransferDtoResult transfer(ExecuteTransferDtoRequest request) {
        ExecuteTransferDtoResult result = new ExecuteTransferDtoResult(request.getSourceAccountNumber(), request.getTargetAccountNumber(), request.getAmount());
        executeTransferValidator.validate(request);
        Account source = accountsService.getAccountByAccountNumber(request.getSourceAccountNumber()).orElseThrow(() -> new AppLogicException("TRANSFER_SOURCE_ACCOUNT_NOT_FOUND", "Перевод невозможен поскольку не существует счет отправителя"));
        Account target = accountsService.getAccountByAccountNumber(request.getTargetAccountNumber()).orElseThrow(() -> new AppLogicException("TRANSFER_TARGET_ACCOUNT_NOT_FOUND", "Перевод невозможен поскольку не существует счет получателя"));
        System.out.println("Source account balance before transfer: " + source.getBalance());
        System.out.println("Target account balance before transfer: " + target.getBalance());
        Transfer transfer = new Transfer(source.getClientId(), source.getAccountNumber(), target.getClientId(), target.getAccountNumber(), request.getAmount());
        transferRepository.save(transfer);
        BigDecimal transferAmount = request.getAmount();
        source.setBalance(source.getBalance().subtract(transferAmount));
        accountsService.saveAccount(source);
        logger.info("Source account balance after transfer: " + source.getBalance());
        target.setBalance(target.getBalance().add(request.getAmount()));
        accountsService.saveAccount(target);
        logger.info("Target account balance after transfer: " + target.getBalance());
        setTransferStatus(transfer, TransferStatus.COMPLETED);

        return result;
    }

    private void setTransferStatus(Transfer transfer, TransferStatus status){
        transfer.setStatus(status);
        transferRepository.save(transfer);
    }
    public List<Transfer> getAllTransfers(Long clientId) {
        return transferRepository.findAllBySourceOrDestinationClientId(clientId);
    }
}
