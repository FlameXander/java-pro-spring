package ru.flamexander.transfer.service.limits.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimitResponse {
    private Long userId;
    private BigDecimal limitAmount;
}
