package ru.flamexander.transfer.service.core.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "ДТО результат выполнения перевода")
public class ExecuteTransferDtoResult {
    @Schema(description = "ID запроса", required = true)
    private Long id;

    @Schema(description = "статус запроса", required = true, example = "создан, в обработке, выполнен, ошибка")
    private String status;

    @Schema(description = "Дата-время результата", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private ZonedDateTime updateDateTime;

}
