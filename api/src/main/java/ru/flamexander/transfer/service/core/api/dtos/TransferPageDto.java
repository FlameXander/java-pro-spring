package ru.flamexander.transfer.service.core.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Отчет о переводах клиента")
public class TransferPageDto {
    @Schema(description = "Список переводов клиента")
    private List<TransferDto> items;
}
