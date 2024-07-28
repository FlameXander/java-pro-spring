package ru.flamexander.transfer.service.core.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "ДТО для отчета по переводам")
public class ReportTransferPageDtoResult {
    @Schema(description = "Список переводов клиента")
    private List<ReportTransferDtoResult> items;
}
