package org.example.projektidemo.service;

import org.example.projektidemo.dto.BudgetDto;

import java.util.List;

public interface BudgetService {
    BudgetDto createBudget(BudgetDto budgetDto);
    List<BudgetDto> getBudgetsByUserId(Long userId);
    List<BudgetDto> getBudgetsByCategoryId(Long categoryId);
    BudgetDto getBudgetByUserIdCategoryId(Long userId, Long categoryId);
    void deleteBudget(Long id);
}
