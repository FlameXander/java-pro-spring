package ru.flamexander.transfer.service.core.api.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransferResponseDto {
    private Long id;
    private Long sourceAccountId;
    private Long targetAccountId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
