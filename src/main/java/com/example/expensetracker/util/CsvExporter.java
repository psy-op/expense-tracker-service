package com.example.expensetracker.util;

import com.example.expensetracker.dto.ExpenseResponse;

import java.io.PrintWriter;
import java.util.List;

public class CsvExporter {
    public static void writeExpenses(List<ExpenseResponse> expenses, PrintWriter writer) {
        writer.println("id,description,amount,date,categoryId,categoryName");
        for (ExpenseResponse e : expenses) {
            writer.printf("%d,%s,%s,%s,%s,%s%n",
                    e.getId(), sanitize(e.getDescription()), e.getAmount(), e.getDate(), e.getCategoryId(), sanitize(e.getCategoryName()));
        }
    }

    private static String sanitize(String v) { return v==null?"": v.replace(',', ';'); }
}
