package ru.flamexander.transfer.service.core.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.flamexander.transfer.service.core.api.dtos.TransferDto;
import ru.flamexander.transfer.service.core.api.dtos.TransferResponseDto;
import ru.flamexander.transfer.service.core.backend.entities.Account;
import ru.flamexander.transfer.service.core.backend.entities.Transfer;
import ru.flamexander.transfer.service.core.backend.errors.AppLogicException;
import ru.flamexander.transfer.service.core.backend.repositories.AccountsRepository;
import ru.flamexander.transfer.service.core.backend.repositories.TransferRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountsService accountsService;
    private final AccountsRepository accountsRepository;
    private final TransferRepository transferRepository;

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class.getName());

    @Transactional
    public TransferResponseDto transfer(TransferDto transferDto) {
        Account source = accountsService.getAccountById(1L, transferDto.getSourceAccountId())
                .orElseThrow(() -> new AppLogicException("TRANSFER_SOURCE_ACCOUNT_NOT_FOUND", "Перевод невозможен, поскольку не существует счет отправителя"));
        Account target = accountsService.getAccountById(1L, transferDto.getTargetAccountId())
                .orElseThrow(() -> new AppLogicException("TRANSFER_TARGET_ACCOUNT_NOT_FOUND", "Перевод невозможен, поскольку не существует счет получателя"));

        BigDecimal amount = transferDto.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppLogicException("INVALID_TRANSFER_AMOUNT", "Сумма перевода должна быть положительной");
        }

        if (source.getBalance().compareTo(amount) < 0) {
            throw new AppLogicException("INSUFFICIENT_FUNDS", "Недостаточно средств на счете отправителя");
        }

        source.setBalance(source.getBalance().subtract(amount));
        target.setBalance(target.getBalance().add(amount));
        accountsRepository.save(source);
        accountsRepository.save(target);

        Transfer transfer = new Transfer();
        transfer.setSourceAccountId(source.getId());
        transfer.setTargetAccountId(target.getId());
        transfer.setAmount(amount);
        transfer.setStatus("COMPLETED");
        transfer.setCreatedAt(LocalDateTime.now());
        transfer.setUpdatedAt(LocalDateTime.now());
        transfer = transferRepository.save(transfer);

        logger.info("Transfer from account id {} to account id {} with amount {} completed successfully.", source.getId(), target.getId(), amount);

        TransferResponseDto responseDto = new TransferResponseDto();
        responseDto.setId(transfer.getId());
        responseDto.setSourceAccountId(transfer.getSourceAccountId());
        responseDto.setTargetAccountId(transfer.getTargetAccountId());
        responseDto.setAmount(transfer.getAmount());
        responseDto.setStatus(transfer.getStatus());
        responseDto.setCreatedAt(transfer.getCreatedAt());
        responseDto.setUpdatedAt(transfer.getUpdatedAt());

        return responseDto;
    }

    public List<TransferResponseDto> getTransfersByClientId(Long clientId) {
        List<Account> accounts = accountsRepository.findAllByClientId(clientId);
        Set<Transfer> transfers = new HashSet<>();

        for (Account account : accounts) {
            transfers.addAll(transferRepository.findAllBySourceAccountId(account.getId()));
            transfers.addAll(transferRepository.findAllByTargetAccountId(account.getId()));
        }

        return transfers.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private TransferResponseDto convertToDto(Transfer transfer) {
        TransferResponseDto responseDto = new TransferResponseDto();
        responseDto.setId(transfer.getId());
        responseDto.setSourceAccountId(transfer.getSourceAccountId());
        responseDto.setTargetAccountId(transfer.getTargetAccountId());
        responseDto.setAmount(transfer.getAmount());
        responseDto.setStatus(transfer.getStatus());
        responseDto.setCreatedAt(transfer.getCreatedAt());
        responseDto.setUpdatedAt(transfer.getUpdatedAt());
        return responseDto;
    }
}

