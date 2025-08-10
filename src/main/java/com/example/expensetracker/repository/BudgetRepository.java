package com.example.expensetracker.repository;

import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserAndMonthAndYear(User user, int month, int year);
}
