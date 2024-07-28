package ru.flamexander.transfer.service.core.backend.validators;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.flamexander.transfer.service.core.api.dtos.ExecuteTransferDtoRequest;
import ru.flamexander.transfer.service.core.backend.entities.Account;
import ru.flamexander.transfer.service.core.backend.errors.FieldValidationError;
import ru.flamexander.transfer.service.core.backend.errors.FieldsValidationException;
import ru.flamexander.transfer.service.core.backend.repositories.AccountsRepository;
import ru.flamexander.transfer.service.core.backend.services.TransferService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
private Long senderId;
    private String sendAccount;
    private Long receiverId;
    private String receivAccount;
    private BigDecimal amount;

    request.getSenderId(),
                request.getSendAccount(),
                request.getReceiverId(),
                request.getReceivAccount(),
                request.getAmount());
    public FieldValidationError(String fieldName, String message) {
    }
 */
@Component
@AllArgsConstructor
public class ExecuteTransferValidator {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteTransferValidator.class);
    private final AccountsRepository accountsRepository;

    public void validate(ExecuteTransferDtoRequest request) {
        logger.info("validate1 request:  {}", request);
        Account account = null;
        List<FieldValidationError> errorFields = new ArrayList<>();
        if (request.getAmount().compareTo(new BigDecimal(0)) <= 0)
            errorFields.add(new FieldValidationError("amount", "Сумма перевода должна быть > 0"));
        logger.info("validate2 amount:  {}", request.getAmount());
        account = accountsRepository.findByAccountNumberAndClientId(request.getReceivAccount(), request.getReceiverId()).orElse(null);
        if (account == null)
            errorFields.add(new FieldValidationError("receivAccount", "Не найден счет получателя: " + request.getReceivAccount()));
        logger.info("validate3 Receiver:  {}", account);
        account = accountsRepository.findByAccountNumberAndClientId(request.getSendAccount(), request.getSenderId()).orElse(null);
        if (account == null)
            errorFields.add(new FieldValidationError("sendAccount", "Не найден клиентский счет: " + request.getSendAccount()));
        logger.info("validate4 Sender:  {}", account);
        if (request.getAmount().compareTo(account.getBalance()) > 0)
            errorFields.add(new FieldValidationError("amount", "Сумма перевода больше остатка на счете!"));
        logger.info("validate5 Balance:  {}", account.getBalance());
        if (!errorFields.isEmpty()) {
            throw new FieldsValidationException(errorFields);
        }
    }
}

/***
 - Перевод осуществляется по номеру счета
 - Нельзя перевести средства на несуществующий счет
 - Нельзя перевести средств больше, чем есть на счете отправителя
 - Нельзя перевести отрицательное количество средств (затянуть к себе средства с чужого счета)
 */


