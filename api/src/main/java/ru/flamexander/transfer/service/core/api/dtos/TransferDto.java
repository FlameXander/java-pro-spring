package ru.flamexander.transfer.service.core.api.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferDto {
    private Long sourceAccountId;
    private Long targetAccountId;
    private BigDecimal amount;
}
