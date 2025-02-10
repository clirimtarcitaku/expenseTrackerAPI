package org.example.projektidemo.mapper;

import org.example.projektidemo.dto.ExpenseDto;
import org.example.projektidemo.entity.Category;
import org.example.projektidemo.entity.Expense;
import org.example.projektidemo.entity.User;

public class ExpenseMapper {
    public static ExpenseDto mapToExpenseDto(Expense expense) {
        return new ExpenseDto(
                expense.getId(),
                expense.getUser().getId(),
                expense.getCategory().getId(),
                expense.getAmount(),
                expense.getDate()
        );
    }

    public static Expense mapToExpense(ExpenseDto expenseDto, User user, Category category) {
        return new Expense(
                expenseDto.getId(),
                user,
                category,
                expenseDto.getAmount(),
                expenseDto.getExpenseDate()
        );
    }
}
