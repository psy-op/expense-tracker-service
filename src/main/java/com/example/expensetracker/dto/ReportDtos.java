package com.example.expensetracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class MonthlyReportResponse {
    private int month;
    private int year;
    private BigDecimal total;
    private Map<String, BigDecimal> byCategory;
    private BigDecimal budget;
    private double percentUsed;
}
