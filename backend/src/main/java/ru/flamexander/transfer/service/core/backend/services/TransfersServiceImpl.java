package ru.flamexander.transfer.service.core.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.flamexander.transfer.service.core.api.dtos.ExecuteTransferDtoRequest;
import ru.flamexander.transfer.service.core.backend.dtos.LimitsInfoRequestDto;
import ru.flamexander.transfer.service.core.backend.dtos.LimitsInfoResponseDto;
import ru.flamexander.transfer.service.core.backend.entities.Account;
import ru.flamexander.transfer.service.core.backend.errors.AppLogicException;
import ru.flamexander.transfer.service.core.backend.integrations.LimitsInfoServiceIntegration;
import ru.flamexander.transfer.service.core.backend.validators.ExecuteTransferValidator;

@Service
@RequiredArgsConstructor
public class TransfersServiceImpl implements TransfersService {
    private final AccountsService accountsService;
    private final ExecuteTransferValidator executeTransferValidator;
    private final ClientsInfoService clientsInfoService;
    private final LimitsInfoServiceIntegration limitsServiceIntegration;

    @Transactional
    @Override
    public void execute(Long clientId, ExecuteTransferDtoRequest executeTransferDtoRequest) {
        if (clientsInfoService.isClientBlocker(clientId)) {
            throw new AppLogicException("SENDER_IS_BLOCKED", "Клиент-отправитель id = " + clientId + " не может выполнить отправку перевода, так как заблокирован");
        }
        if (clientsInfoService.isClientBlocker(executeTransferDtoRequest.getReceiverId())) {
            throw new AppLogicException("RECEIVER_IS_BLOCKED", "Невозможно выполнить перевод заблокированному клиенту с id = " + executeTransferDtoRequest.getReceiverId());
        }

        Account senderAccount = accountsService.findByClientIdAndAccountNumber(clientId, executeTransferDtoRequest.getSenderAccountNumber())
                .orElseThrow(() -> new AppLogicException("TRANSFER_SOURCE_ACCOUNT_NOT_FOUND", "Перевод невозможен, поскольку не существует счет отправителя"));
        Account receiverAccount = accountsService.findByClientIdAndAccountNumber(executeTransferDtoRequest.getReceiverId(), executeTransferDtoRequest.getReceiverAccountNumber())
                .orElseThrow(() -> new AppLogicException("TRANSFER_TARGET_ACCOUNT_NOT_FOUND", "Перевод невозможен, поскольку не существует счет получателя"));

        LimitsInfoRequestDto limitsRequestDto = new LimitsInfoRequestDto(clientId, executeTransferDtoRequest.getTransferSum());
        LimitsInfoResponseDto limitsResponse = limitsServiceIntegration.deductClientLimit(limitsRequestDto);

        senderAccount.setBalance(senderAccount.getBalance().subtract(executeTransferDtoRequest.getTransferSum()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(executeTransferDtoRequest.getTransferSum()));

        // Здесь можно добавить логику rollback, в случае ошибки при сохранении балансов
        try {
        } catch (Exception ex) {
            limitsServiceIntegration.rollbackClientLimit(limitsRequestDto);
            throw ex;
        }
    }
}
