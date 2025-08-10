package com.example.expensetracker.service;

import com.example.expensetracker.dto.MonthlyReportResponse;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.BudgetRepository;
import com.example.expensetracker.repository.ExpenseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;
    private final EntityManager em;

    public MonthlyReportResponse monthly(User user, Integer month, Integer year) {
        YearMonth ym = YearMonth.of(year==null?YearMonth.now().getYear():year, month==null?YearMonth.now().getMonthValue():month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        BigDecimal total = expenseRepository.totalForUserBetween(user, start, end);
        @SuppressWarnings("unchecked")
        var query = em.createQuery("select c.name as name, coalesce(sum(e.amount),0) as total from Expense e join e.category c where e.user = :u and e.date between :s and :e group by c.name", Tuple.class)
                .setParameter("u", user).setParameter("s", start).setParameter("e", end).getResultList();
        Map<String, BigDecimal> byCategory = query.stream().collect(Collectors.toMap(t -> t.get("name", String.class), t -> t.get("total", BigDecimal.class)));
        Budget budget = budgetRepository.findByUserAndMonthAndYear(user, ym.getMonthValue(), ym.getYear()).orElse(null);
        MonthlyReportResponse resp = new MonthlyReportResponse();
        resp.setMonth(ym.getMonthValue());
        resp.setYear(ym.getYear());
        resp.setTotal(total);
        resp.setByCategory(byCategory);
        resp.setBudget(budget==null? BigDecimal.ZERO: budget.getAmount());
        resp.setPercentUsed(resp.getBudget().signum()==0?0: total.multiply(BigDecimal.valueOf(100)).divide(resp.getBudget(),2, java.math.RoundingMode.HALF_UP).doubleValue());
        return resp;
    }
}
