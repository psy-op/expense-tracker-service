package com.example.expensetracker.controller;

import com.example.expensetracker.dto.MonthlyReportResponse;
import com.example.expensetracker.model.User;
import com.example.expensetracker.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/monthly")
    public MonthlyReportResponse monthly(@AuthenticationPrincipal User user,
                                         @RequestParam(required = false) Integer month,
                                         @RequestParam(required = false) Integer year) {
        return reportService.monthly(user, month, year);
    }
}
