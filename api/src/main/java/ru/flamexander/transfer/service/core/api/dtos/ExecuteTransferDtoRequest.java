package ru.flamexander.transfer.service.core.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@Schema(description = "ДТО запроса выполнения перевода")
public class ExecuteTransferDtoRequest {
    @Schema(description = "ID запроса", required = true)
    private Long id;

    @Schema(description = "ID отправителя", required = true)
    private Long senderId;

    @Schema(description = "Номер счета отправителя", required = true, minLength = 16, maxLength = 16, example = "1234123412341234")
    private String sendAccount;

    @Schema(description = "ID получателя", required = true)
    private Long receiverId;

    @Schema(description = "Номер счета получателя", required = true, minLength = 16, maxLength = 16, example = "1234123412341234")
    private String receivAccount;

    @Schema(description = "сумма перевода", required = true, example = "1000.99")
    private BigDecimal amount;
}
