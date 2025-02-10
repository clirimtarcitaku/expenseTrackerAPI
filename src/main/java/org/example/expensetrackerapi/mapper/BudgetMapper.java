package org.example.projektidemo.mapper;

import org.example.projektidemo.dto.BudgetDto;
import org.example.projektidemo.entity.Budget;
import org.example.projektidemo.entity.Category;
import org.example.projektidemo.entity.User;

public class BudgetMapper {
    public static BudgetDto mapToBudgetDto(Budget budget) {
        return new BudgetDto(
                budget.getId(),
                budget.getUser().getId(),
                budget.getCategory().getId(),
                budget.getBudgetAmount(),
                budget.getCreatedAt()
        );
    }

    public static Budget mapToBudget(BudgetDto budgetDto, User user, Category category) {
        return new Budget(
                budgetDto.getId(),
                user,
                category,
                budgetDto.getBudgetAmount(),
                budgetDto.getCreatedAt()
        );
    }
}
