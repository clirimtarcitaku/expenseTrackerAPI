package org.example.projektidemo.repository;

import org.example.projektidemo.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);
    List<Expense> findExpensesByCategory_Id(Long categoryId);
    Expense findFirstByUserIdOrderByDateAsc(Long userId);
    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId " +
            "AND (:categoryId IS NULL OR e.category.id = :categoryId) " +
            "AND (:fromDate IS NULL OR e.date >= :fromDate) " +
            "AND (:toDate IS NULL OR e.date <= :toDate)" +
            "ORDER BY e.amount DESC")
    List<Expense> findExpensesWithFilters(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

}
