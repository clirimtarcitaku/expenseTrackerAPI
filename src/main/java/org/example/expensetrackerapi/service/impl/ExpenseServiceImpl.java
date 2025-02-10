package org.example.projektidemo.service.impl;

import lombok.AllArgsConstructor;
import org.example.projektidemo.dto.ExpenseDto;
import org.example.projektidemo.entity.Budget;
import org.example.projektidemo.entity.Category;
import org.example.projektidemo.entity.Expense;
import org.example.projektidemo.entity.User;
import org.example.projektidemo.exception.DateException;
import org.example.projektidemo.exception.NotEnoughFundsException;
import org.example.projektidemo.exception.ResourceNotFoundException;
import org.example.projektidemo.mapper.ExpenseMapper;
import org.example.projektidemo.repository.BudgetRepository;
import org.example.projektidemo.repository.CategoryRepository;
import org.example.projektidemo.repository.ExpenseRepository;
import org.example.projektidemo.repository.UserRepository;
import org.example.projektidemo.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final BudgetRepository budgetRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private ExpenseRepository expenseRepository;

    @Override
    public ExpenseDto createExpense(ExpenseDto expenseDto) {
        User user = userRepository.findById(expenseDto.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found by ID: " + expenseDto.getUserId())
        );
        Category category = categoryRepository.findById(expenseDto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category not found by ID: " + expenseDto.getCategoryId())
        );
        Budget userBudget = budgetRepository.findByCategoryIdAndUserId(category.getId(), user.getId());
        try{
            if(userBudget == null || userBudget.getBudgetAmount() < expenseDto.getAmount()) {
                throw new NotEnoughFundsException("Not enough funds!");
            }
        }catch(NotEnoughFundsException nefe){
            System.out.println(nefe.getMessage());
            return null;
        }
        Expense expense = ExpenseMapper.mapToExpense(expenseDto, user, category);
        Expense savedExpense = expenseRepository.save(expense);
        updateBudget(userBudget, savedExpense.getAmount());
        return ExpenseMapper.mapToExpenseDto(savedExpense);
    }

    @Override
    public List<ExpenseDto> getAllExpenses(){
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream().map(ExpenseMapper::mapToExpenseDto).collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDto> getExpensesByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found by ID: " + userId)
        );
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        return expenses.stream().map(ExpenseMapper::mapToExpenseDto).collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDto> getExpensesByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category not found by ID: " + categoryId)
        );
        List<Expense> expenses = expenseRepository.findExpensesByCategory_Id(categoryId);
        return expenses.stream().map(ExpenseMapper::mapToExpenseDto).collect(Collectors.toList());
    }

    @Override
    public void deleteExpense(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(
                () -> new ResourceNotFoundException("Expense not found by ID: " + expenseId)
        );
        expenseRepository.delete(expense);
    }

    private List<Expense> filterQuery(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found by ID: " + userId)
        );
        if ((fromDate != null && toDate != null)) {
            try{
                if(toDate.isBefore(fromDate)) {
                    throw new DateException("First date cannot be before second date!");
                }
            }catch (DateException de){
                System.out.println(de.getMessage());
                return null;
            }
        }
        List<Expense> expenses = expenseRepository.findExpensesWithFilters(userId, categoryId, fromDate, toDate);

        return expenses;
    }

    @Override
    public ExpenseDto getMostExpensiveExpense(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate) {
        List<Expense> expenses = filterQuery(userId, categoryId, fromDate, toDate);
        try{
            if(expenses.isEmpty()) {
                throw new ResourceNotFoundException("Expense not found!");
            }
        }catch (ResourceNotFoundException e){
            System.out.println(e.getMessage());
            return null;
        }
        Expense finalExpense = expenses.get(0);
        return ExpenseMapper.mapToExpenseDto(finalExpense);
    }

    @Override
    public ExpenseDto getLeastExpensiveExpense(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate) {
        List<Expense> expenses = filterQuery(userId, categoryId, fromDate, toDate);
        try{
            if(expenses.isEmpty()) {
                throw new ResourceNotFoundException("Expense not found!");
            }
        }catch (ResourceNotFoundException e){
            System.out.println(e.getMessage());
            return null;
        }
        int listLength = expenses.size();
        Expense finalExpense = expenses.get(listLength - 1);
        return ExpenseMapper.mapToExpenseDto(finalExpense);
    }

    @Override
    public void updateBudget(Budget userBudget, double expenseAmount) {
        userBudget.setBudgetAmount(userBudget.getBudgetAmount() - expenseAmount);
        budgetRepository.save(userBudget);
    }

    @Override
    public ExpenseDto updateExpense(Long expenseId, ExpenseDto expenseDto) {
        Expense existingExpense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found by ID: " + expenseId));

        User user = userRepository.findById(expenseDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found by ID: " + expenseDto.getUserId()));

        Category category = categoryRepository.findById(expenseDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found by ID: " + expenseDto.getCategoryId()));

        Budget userBudget = budgetRepository.findByCategoryIdAndUserId(existingExpense.getCategory().getId(), user.getId());
        if (userBudget != null) {
            userBudget.setBudgetAmount(userBudget.getBudgetAmount() + existingExpense.getAmount());
        }

        Budget newCategoryBudget = budgetRepository.findByCategoryIdAndUserId(category.getId(), user.getId());
        try{
            if (newCategoryBudget == null || newCategoryBudget.getBudgetAmount() < expenseDto.getAmount()) {
                throw new NotEnoughFundsException("Not enough funds for the updated category!");
            }
        }catch (NotEnoughFundsException e){
            System.out.println(e.getMessage());
            return null;
        }

        existingExpense.setCategory(category);
        existingExpense.setAmount(expenseDto.getAmount());
        existingExpense.setDate(expenseDto.getExpenseDate() != null ? expenseDto.getExpenseDate() : existingExpense.getDate());

        Expense updatedExpense = expenseRepository.save(existingExpense);

        newCategoryBudget.setBudgetAmount(newCategoryBudget.getBudgetAmount() - updatedExpense.getAmount());
        budgetRepository.save(newCategoryBudget);

        return ExpenseMapper.mapToExpenseDto(updatedExpense);
    }

    private double calculateAverageExpense(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate, ChronoUnit unit) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found by ID: " + userId)
        );
        List<Expense> expenses = expenseRepository.findExpensesWithFilters(userId, categoryId, fromDate, toDate);
        if (expenses.isEmpty()) {
            return 0.0;
        }
        LocalDate start = fromDate != null ? fromDate : expenseRepository.findFirstByUserIdOrderByDateAsc(userId).getDate();
        LocalDate end = toDate != null ? toDate : LocalDate.now();
        try {
            if (toDate.isBefore(fromDate)) {
                throw new DateException("First date cannot be before second date!");
            }
        } catch (DateException de) {
            System.out.println(de.getMessage());
            return 0;
        }
        long timeBetween = unit.between(start, end) + 1;
        if (timeBetween <= 0) {
            return 0.0;
        }
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        return totalExpenses / timeBetween;
    }

    @Override
    public double getAverageDailyExpense(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate) {
        return calculateAverageExpense(userId, categoryId, fromDate, toDate, ChronoUnit.DAYS);
    }

    @Override
    public double getAverageMonthlyExpense(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate) {
        return calculateAverageExpense(userId, categoryId, fromDate, toDate, ChronoUnit.MONTHS);
    }

    @Override
    public double getAverageYearlyExpense(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate) {
        return calculateAverageExpense(userId, categoryId, fromDate, toDate, ChronoUnit.YEARS);
    }

    @Override
    public double getTotalExpense(Long userId, Long categoryId, LocalDate fromDate, LocalDate toDate) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found by ID: " + userId)
        );
        List<Expense> expenses = expenseRepository.findExpensesWithFilters(userId, categoryId, fromDate, toDate);
        if (expenses.isEmpty()) {
            return 0.0;
        }

        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();

        return totalExpenses;
    }
}
