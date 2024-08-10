package ru.flamexander.transfer.service.core.backend.validators;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.flamexander.transfer.service.core.backend.entities.Account;
import ru.flamexander.transfer.service.core.api.dtos.ExecuteTransferDtoRequest;
import ru.flamexander.transfer.service.core.backend.errors.FieldValidationError;
import ru.flamexander.transfer.service.core.backend.errors.FieldsValidationException;
import ru.flamexander.transfer.service.core.backend.repositories.AccountsRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ExecuteTransferValidator {
    private final AccountsRepository accountsRepository;
    public void validate(ExecuteTransferDtoRequest request) {
        Account account = null;
        List<FieldValidationError> errorFields = new ArrayList<>();
        if (request.getAmount().compareTo(account.getBalance()) <= 0) {
            errorFields.add(new FieldValidationError("amount", "Нельзя перевести средств больше, чем есть на счете отправителя"));
        }
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errorFields.add(new FieldValidationError("amount", "Нельзя перевести отрицательное или нулевое количество средств"));
        }
        if (!errorFields.isEmpty()) {
            throw new FieldsValidationException(errorFields);
        }
    }
}
