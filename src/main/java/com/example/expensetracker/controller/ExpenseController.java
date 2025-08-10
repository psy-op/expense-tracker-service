package com.example.expensetracker.controller;

import com.example.expensetracker.dto.*;
import com.example.expensetracker.model.User;
import com.example.expensetracker.service.ExpenseService;
import com.example.expensetracker.util.CsvExporter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    public ExpenseResponse create(@Valid @RequestBody ExpenseCreateRequest req, @AuthenticationPrincipal User user) {
        return expenseService.create(req, user);
    }

    @GetMapping
    public Page<ExpenseResponse> list(@AuthenticationPrincipal User user,
                                      @RequestParam(required = false) Long categoryId,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                      @RequestParam(required = false) BigDecimal minAmount,
                                      @RequestParam(required = false) BigDecimal maxAmount,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size,
                                      @RequestParam(defaultValue = "date,desc") String sort) {
        String[] parts = sort.split(",");
        Sort s = parts.length==2? Sort.by(Sort.Direction.fromString(parts[1]), parts[0]) : Sort.by("date").descending();
        Pageable pageable = PageRequest.of(page, size, s);
        return expenseService.search(categoryId, startDate, endDate, minAmount, maxAmount, user, pageable);
    }

    @PutMapping("/{id}")
    public ExpenseResponse update(@PathVariable Long id, @RequestBody ExpenseUpdateRequest req, @AuthenticationPrincipal User user) {
        return expenseService.update(id, req, user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        expenseService.delete(id, user);
    }

    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<byte[]> export(@AuthenticationPrincipal User user) {
        // export last 500 expenses for now
        Page<ExpenseResponse> page = expenseService.search(null, null, null, null, null, user, PageRequest.of(0, 500, Sort.by("date").descending()));
        List<ExpenseResponse> list = page.getContent();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
        CsvExporter.writeExpenses(list, new java.io.PrintWriter(writer, true));
        byte[] bytes = baos.toByteArray();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expenses.csv")
                .contentLength(bytes.length)
                .contentType(MediaType.valueOf("text/csv"))
                .body(bytes);
    }
}
