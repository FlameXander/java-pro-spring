package ru.flamexander.transfer.service.core.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.flamexander.transfer.service.core.api.dtos.CreateAccountDto;
import ru.flamexander.transfer.service.core.backend.entities.Account;
import ru.flamexander.transfer.service.core.backend.errors.AppLogicException;
import ru.flamexander.transfer.service.core.backend.repositories.AccountsRepository;


import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountsService {
    private final AccountsRepository accountsRepository;
    private static final Logger logger = LoggerFactory.getLogger(AccountsService.class.getName());

    public List<Account> getAllAccounts(Long clientId) {
        return accountsRepository.findAllByClientId(clientId);
    }

    public Optional<Account> getAccountById(Long clientId, Long id) {
        return accountsRepository.findByIdAndClientId(id, clientId);
    }
    public Optional<Account> getAccountByAccountNumber(String accountNumber){
        return accountsRepository.findByAccountNumber(accountNumber);
    }

    public Account createNewAccount(Long clientId, CreateAccountDto createAccountDto) {
        if (createAccountDto.getInitialBalance() == null) {
            throw new AppLogicException("VALIDATION_ERROR", "Создаваемый счет не может иметь null баланс");
        }

        Account account = new Account(clientId, createAccountDto.getInitialBalance());
        account.setAccountNumber("1234567891234567");

        Account savedAccount = accountsRepository.save(account);
        if (savedAccount == null) {
            throw new AppLogicException("DATABASE_ERROR", "Не удалось сохранить счет в базе данных");
        }

        logger.info("Account id = {} created from {}", savedAccount.getId(), createAccountDto);
        return savedAccount;
    }
    public void saveAccount(Account account) {
        if (account != null) {
            accountsRepository.save(account);
        } else {
            // Логирование ошибки
            System.out.println("Attempted to save a null account");
        }
    }
}
