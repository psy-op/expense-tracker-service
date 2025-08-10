package com.example.expensetracker.service;

import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> list(User user) {
        return categoryRepository.findByUserIsNullOrUser(user);
    }

    public Category create(String name, User user) {
        Category category = Category.builder().name(name).user(user).build();
        return categoryRepository.save(category);
    }

    public Category get(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }
}
