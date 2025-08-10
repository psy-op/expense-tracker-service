package com.example.expensetracker.config;

import com.example.expensetracker.model.Category;
import com.example.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            List.of("Food", "Rent", "Utilities", "Entertainment", "Travel", "Misc").forEach(name -> categoryRepository.save(Category.builder().name(name).build()));
        }
    }
}
