package ru.flamexander.transfer.service.core.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Schema(description = "ДТО результат выполнения перевода")
public class ExecuteTransferDtoResult {
    private Long id;
    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal transferAmount;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime dateTime;

    public ExecuteTransferDtoResult(String sourceAccount, String destinationAccount,
                                    BigDecimal transferAmount) {

        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.transferAmount = transferAmount;
        this.dateTime = LocalDateTime.now();
    }
}
