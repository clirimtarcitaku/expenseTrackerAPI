package org.example.projektidemo.service.impl;

import lombok.AllArgsConstructor;
import org.example.projektidemo.dto.BudgetDto;
import org.example.projektidemo.entity.Budget;
import org.example.projektidemo.entity.Category;
import org.example.projektidemo.entity.User;
import org.example.projektidemo.exception.ResourceNotFoundException;
import org.example.projektidemo.mapper.BudgetMapper;
import org.example.projektidemo.repository.BudgetRepository;
import org.example.projektidemo.repository.CategoryRepository;
import org.example.projektidemo.repository.UserRepository;
import org.example.projektidemo.service.BudgetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private BudgetRepository budgetRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    @Override
    public BudgetDto createBudget(BudgetDto budgetDto) {
        User user = userRepository.findById(budgetDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        Category category = categoryRepository.findById(budgetDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

        Budget budget = BudgetMapper.mapToBudget(budgetDto, user, category);
        Budget savedBudget = budgetRepository.save(budget);
        return BudgetMapper.mapToBudgetDto(savedBudget);
    }

    @Override
    public List<BudgetDto> getBudgetsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found!")
        );
        List<Budget> budgets = budgetRepository.findByUserId(userId);
        return budgets.stream().map(BudgetMapper::mapToBudgetDto).collect(Collectors.toList());
    }

    @Override
    public List<BudgetDto> getBudgetsByCategoryId(Long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category not found!")
        );
        List<Budget> budgets = budgetRepository.findByCategoryId(categoryId);
        return budgets.stream().map(BudgetMapper::mapToBudgetDto).collect(Collectors.toList());
    }

    @Override
    public BudgetDto getBudgetByUserIdCategoryId(Long userId, Long categoryId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found!")
        );
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category not found!")
        );
        Budget budget = budgetRepository.findByCategoryIdAndUserId(categoryId, userId);
        return BudgetMapper.mapToBudgetDto(budget);
    }


    @Override
    public void deleteBudget(Long id) {
        Budget budget = budgetRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Budget not found! ID: " + id)
        );
        budgetRepository.delete(budget);
    }
}
