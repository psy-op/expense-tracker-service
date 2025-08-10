package com.example.expensetracker.repository;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("select e from Expense e where e.user = :user " +
            "and (:categoryId is null or e.category.id = :categoryId) " +
            "and (:startDate is null or e.date >= :startDate) " +
            "and (:endDate is null or e.date <= :endDate) " +
            "and (:minAmount is null or e.amount >= :minAmount) " +
            "and (:maxAmount is null or e.amount <= :maxAmount)")
+    Page<Expense> search(@Param("user") User user,
+                         @Param("categoryId") Long categoryId,
+                         @Param("startDate") LocalDate startDate,
+                         @Param("endDate") LocalDate endDate,
+                         @Param("minAmount") BigDecimal minAmount,
+                         @Param("maxAmount") BigDecimal maxAmount,
+                         Pageable pageable);

    @Query("select coalesce(sum(e.amount),0) from Expense e where e.user = :user and e.date between :start and :end")
    BigDecimal totalForUserBetween(User user, LocalDate start, LocalDate end);
}
