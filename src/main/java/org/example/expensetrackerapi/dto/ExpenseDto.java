package org.example.projektidemo.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDto {
    private Long id;
    private Long userId;
    private Long categoryId;
    private double amount;
    private LocalDate expenseDate;
}
