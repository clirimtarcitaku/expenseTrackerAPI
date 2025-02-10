package org.example.projektidemo.service;

import org.example.projektidemo.dto.ExpenseDto;
import org.example.projektidemo.entity.Budget;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    ExpenseDto createExpense(ExpenseDto expenseDto);
    List<ExpenseDto> getAllExpenses();
    List<ExpenseDto> getExpensesByUserId(Long userId);
    List<ExpenseDto> getExpensesByCategoryId(Long categoryId);
    void updateBudget(Budget userBudget, double expenseAmount);
    ExpenseDto updateExpense(Long expenseId, ExpenseDto expenseDto);
    void deleteExpense(Long id);
    ExpenseDto getMostExpensiveExpense(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate);
    ExpenseDto getLeastExpensiveExpense(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate);
    double getAverageDailyExpense(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate);
    double getAverageMonthlyExpense(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate);
    double getAverageYearlyExpense(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate);
    double getTotalExpense(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate);
}
