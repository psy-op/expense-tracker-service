package com.example.expensetracker.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetSetRequest {
    @Min(1) @Max(12)
    private int month;
    private int year; // 0 => current year (handled in service)
    @NotNull @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;
}

@Data
class BudgetUsageResponse {
    private int month;
    private int year;
    private BigDecimal budget;
    private BigDecimal spent;
    private double percentUsed; // 0-100
}
