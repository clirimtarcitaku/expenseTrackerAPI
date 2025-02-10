package org.example.projektidemo.repository;

import org.example.projektidemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT e.user.id, SUM(e.amount) as totalExpenses FROM Expense e " +
            "GROUP BY e.user.id " +
            "ORDER BY totalExpenses DESC LIMIT 1")
    Object userWithLargestExpenses();
}
