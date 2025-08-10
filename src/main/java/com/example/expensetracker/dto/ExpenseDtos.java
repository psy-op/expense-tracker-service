package com.example.expensetracker.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseCreateRequest {
    @NotBlank
    private String description;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;
    @NotNull
    private LocalDate date;
    private Long categoryId; // optional
}

@Data
class ExpenseUpdateRequest {
    private String description;
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;
    private LocalDate date;
    private Long categoryId;
}

@Data
class ExpenseResponse {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private Long categoryId;
    private String categoryName;
}
