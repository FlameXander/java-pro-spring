package ru.flamexander.transfer.service.core.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@NoArgsConstructor
@Data
@Schema(description = "Документы переводов")
public class ReportTransferDtoResult {

    @Schema(description = "Направление перевода", required = true)
    private Boolean direction;

    @Schema(description = "Номер счета клиента", example = "1234123412341234", required = true)
    private String clientAccount;

    @Schema(description = "ID контрагента", required = true)
    private Long contractorId;

    @Schema(description = "Номер счета контрагента", required = true, minLength = 16, maxLength = 16, example = "1234123412341234")
    private String contractorAccount;

    @Schema(description = "сумма перевода", required = true, example = "1000.99")
    private BigDecimal amount;

    @Schema(description = "статус запроса", required = true, example = "создан, в обработке, выполнен, ошибка")
    private String documentStatus;

    @Schema(description = "Дата-время результата", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private ZonedDateTime updateDateTime;

    public ReportTransferDtoResult(String clientAccount, Long contractorId, String contractorAccount, BigDecimal amount, String documentStatus, ZonedDateTime updateDateTime) {
        this.clientAccount = clientAccount;
        this.contractorId = contractorId;
        this.contractorAccount = contractorAccount;
        this.amount = amount;
        this.documentStatus = documentStatus;
        this.updateDateTime = updateDateTime;
    }
}
