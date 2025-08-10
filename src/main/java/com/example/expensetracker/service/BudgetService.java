package com.example.expensetracker.service;

import com.example.expensetracker.dto.BudgetSetRequest;
import com.example.expensetracker.dto.BudgetUsageResponse;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.BudgetRepository;
import com.example.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    @Transactional
    public Budget setBudget(BudgetSetRequest req, User user) {
        YearMonth ym = YearMonth.of(req.getYear() == 0 ? YearMonth.now().getYear() : req.getYear(), req.getMonth());
        Budget budget = budgetRepository.findByUserAndMonthAndYear(user, ym.getMonthValue(), ym.getYear())
                .orElse(Budget.builder().user(user).month(ym.getMonthValue()).year(ym.getYear()).build());
        budget.setAmount(req.getAmount());
        return budgetRepository.save(budget);
    }

    public BudgetUsageResponse currentUsage(User user) {
        YearMonth ym = YearMonth.now();
        Budget budget = budgetRepository.findByUserAndMonthAndYear(user, ym.getMonthValue(), ym.getYear()).orElse(null);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        BigDecimal spent = expenseRepository.totalForUserBetween(user, start, end);
        BudgetUsageResponse resp = new BudgetUsageResponse();
        resp.setMonth(ym.getMonthValue());
        resp.setYear(ym.getYear());
        resp.setSpent(spent);
        resp.setBudget(budget == null ? BigDecimal.ZERO : budget.getAmount());
        resp.setPercentUsed(resp.getBudget().signum()==0?0: spent.multiply(BigDecimal.valueOf(100)).divide(resp.getBudget(), 2, java.math.RoundingMode.HALF_UP).doubleValue());
        return resp;
    }
}
