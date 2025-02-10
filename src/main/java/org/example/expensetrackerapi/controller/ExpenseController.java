package org.example.projektidemo.controller;

import lombok.AllArgsConstructor;
import org.example.projektidemo.dto.ExpenseDto;
import org.example.projektidemo.entity.Budget;
import org.example.projektidemo.exception.ResourceNotFoundException;
import org.example.projektidemo.repository.BudgetRepository;
import org.example.projektidemo.service.ExpenseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@AllArgsConstructor
public class ExpenseController {
    private BudgetRepository budgetRepository;
    private ExpenseService expenseService;

    @PostMapping("create")
    public ResponseEntity<ExpenseDto> createExpense(@RequestBody ExpenseDto expenseDto) {
        Budget budget = budgetRepository.findByCategoryIdAndUserId(expenseDto.getCategoryId(), expenseDto.getUserId());
        if(budget == null) {
            throw new ResourceNotFoundException("Budget not found!");
        }
        ExpenseDto savedExpense = expenseService.createExpense(expenseDto);
        return ResponseEntity.ok(savedExpense);
    }

    @GetMapping("all")
    public ResponseEntity<List<ExpenseDto>> getAllExpenses() {
        List<ExpenseDto> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<List<ExpenseDto>> getExpensesByUser(@PathVariable("id") Long userId) {
        List<ExpenseDto> expenses = expenseService.getExpensesByUserId(userId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("category/{id}")
    public ResponseEntity<List<ExpenseDto>> getExpensesByCategory(@PathVariable("id") Long categoryId) {
        List<ExpenseDto> expenses = expenseService.getExpensesByCategoryId(categoryId);
        return ResponseEntity.ok(expenses);
    }


    @PutMapping("update/{id}")
    public ResponseEntity<ExpenseDto> updateExpense(
            @PathVariable("id") Long expenseId,
            @RequestBody ExpenseDto expenseDto) {

        ExpenseDto updatedExpense = expenseService.updateExpense(expenseId, expenseDto);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable("id") Long expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.ok("Expense deleted successfully!");
    }

    @GetMapping("mostExpensive/{id}")
    public ResponseEntity<ExpenseDto> getMostExpensiveExpense(
            @PathVariable("id") Long userId,
              @RequestParam(value = "category", required = false) Long categoryId,
              @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
              @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ){
        ExpenseDto expenseDto = expenseService.getMostExpensiveExpense(userId, categoryId, fromDate, toDate);
        return ResponseEntity.ok(expenseDto);
    }

    @GetMapping("leastExpensive/{id}")
    public ResponseEntity<ExpenseDto> getLeastExpensiveExpense(
            @PathVariable("id") Long userId,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        ExpenseDto expenseDto = expenseService.getLeastExpensiveExpense(userId, categoryId, fromDate, toDate);
        return ResponseEntity.ok(expenseDto);
    }

    @GetMapping("averageDaily/{id}")
    public ResponseEntity<Double> getAverageDailyExpense(
            @PathVariable("id") Long userId,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ){
        double averageDaily = expenseService.getAverageDailyExpense(userId, categoryId, fromDate, toDate);
        return ResponseEntity.ok(averageDaily);
    }

    @GetMapping("averageMonthly/{id}")
    public ResponseEntity<Double> getAverageMonthlyExpense(
            @PathVariable("id") Long userId,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ){
        double averageMonthly = expenseService.getAverageMonthlyExpense(userId, categoryId, fromDate, toDate);
        return ResponseEntity.ok(averageMonthly);
    }

    @GetMapping("averageYearly/{id}")
    public ResponseEntity<Double> getAverageYearlyExpense(
            @PathVariable("id") Long userId,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ){
        double averageYearly = expenseService.getAverageYearlyExpense(userId, categoryId, fromDate, toDate);
        return ResponseEntity.ok(averageYearly);
    }

    @GetMapping("total/{id}")
    public ResponseEntity<Double> getTotalExpense(
            @PathVariable("id") Long userId,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ){
        double totalExpense = expenseService.getTotalExpense(userId, categoryId, fromDate, toDate);
        return ResponseEntity.ok(totalExpense);
    }
}

















































