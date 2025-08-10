package com.example.expensetracker.controller;

import com.example.expensetracker.dto.BudgetSetRequest;
import com.example.expensetracker.dto.BudgetUsageResponse;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.User;
import com.example.expensetracker.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public Budget set(@Valid @RequestBody BudgetSetRequest req, @AuthenticationPrincipal User user) {
        return budgetService.setBudget(req, user);
    }

    @GetMapping("/current")
    public BudgetUsageResponse current(@AuthenticationPrincipal User user) {
        return budgetService.currentUsage(user);
    }
}
