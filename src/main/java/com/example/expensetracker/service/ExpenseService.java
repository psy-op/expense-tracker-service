package com.example.expensetracker.service;

import com.example.expensetracker.dto.ExpenseCreateRequest;
import com.example.expensetracker.dto.ExpenseResponse;
import com.example.expensetracker.dto.ExpenseUpdateRequest;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;

    private static final Function<Expense, ExpenseResponse> MAPPER = e -> {
        ExpenseResponse r = new ExpenseResponse();
        r.setId(e.getId());
        r.setDescription(e.getDescription());
        r.setAmount(e.getAmount());
        r.setDate(e.getDate());
        if (e.getCategory()!=null) {
            r.setCategoryId(e.getCategory().getId());
            r.setCategoryName(e.getCategory().getName());
        }
        return r;
    };

    @Transactional
    public ExpenseResponse create(ExpenseCreateRequest req, User user) {
        Category category = req.getCategoryId() != null ? categoryService.get(req.getCategoryId()) : null;
        Expense expense = Expense.builder()
                .description(req.getDescription())
                .amount(req.getAmount())
                .date(req.getDate())
                .category(category)
                .user(user)
                .build();
        return MAPPER.apply(expenseRepository.save(expense));
    }

    public Page<ExpenseResponse> search(Long categoryId, java.time.LocalDate startDate, java.time.LocalDate endDate,
                                        java.math.BigDecimal minAmount, java.math.BigDecimal maxAmount, User user, Pageable pageable) {
        return expenseRepository.search(user, categoryId, startDate, endDate, minAmount, maxAmount, pageable).map(MAPPER);
    }

    @Transactional
    public ExpenseResponse update(Long id, ExpenseUpdateRequest req, User user) {
        Expense e = expenseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        if (!e.getUser().getId().equals(user.getId())) throw new IllegalArgumentException("Forbidden");
        if (req.getDescription()!=null) e.setDescription(req.getDescription());
        if (req.getAmount()!=null) e.setAmount(req.getAmount());
        if (req.getDate()!=null) e.setDate(req.getDate());
        if (req.getCategoryId()!=null) e.setCategory(categoryService.get(req.getCategoryId()));
        return MAPPER.apply(e);
    }

    public void delete(Long id, User user) {
        Expense e = expenseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        if (!e.getUser().getId().equals(user.getId())) throw new IllegalArgumentException("Forbidden");
        expenseRepository.delete(e);
    }
}
