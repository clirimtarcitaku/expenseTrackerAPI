package org.example.projektidemo.repository;

import org.example.projektidemo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT e.category.id, COUNT(e.id) as expenseCount " +
            "FROM Expense e " +
            "GROUP BY e.category.id, e.category.categoryName " +
            "ORDER BY expenseCount DESC LIMIT 1")
    Object findMostFrequentCategory();
}
