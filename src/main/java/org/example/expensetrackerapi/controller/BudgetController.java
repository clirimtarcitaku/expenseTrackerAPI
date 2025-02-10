package org.example.projektidemo.controller;

import lombok.AllArgsConstructor;
import org.example.projektidemo.dto.BudgetDto;
import org.example.projektidemo.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@AllArgsConstructor
public class BudgetController {
    private BudgetService budgetService;

    @PostMapping("create")
    public ResponseEntity<BudgetDto> createBudget(@RequestBody BudgetDto budgetDto) {
        BudgetDto savedBudget = budgetService.createBudget(budgetDto);
        return ResponseEntity.ok(savedBudget);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<List<BudgetDto>> getBudgetsByUser(@PathVariable("id") Long userId) {
        List<BudgetDto> budgets = budgetService.getBudgetsByUserId(userId);
        return ResponseEntity.ok(budgets);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteBudget(@PathVariable("id") Long budgetId) {
        budgetService.deleteBudget(budgetId);
        return ResponseEntity.ok("Budget deleted successfully!");
    }

    @GetMapping("category/{id}")
    public ResponseEntity<List<BudgetDto>> getBudgetsByCategory(@PathVariable("id") Long categoryId) {
        List<BudgetDto> budgets = budgetService.getBudgetsByCategoryId(categoryId);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("userCategory")
    public ResponseEntity<BudgetDto> getBudgetByUserAndCategory(
            @RequestBody Long userId,
            @RequestBody Long categoryId) {
        BudgetDto budget = budgetService.getBudgetByUserIdCategoryId(userId, categoryId);
        return ResponseEntity.ok(budgetService.getBudgetByUserIdCategoryId(userId, categoryId));
    }
}
