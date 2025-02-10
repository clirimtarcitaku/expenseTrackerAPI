package org.example.projektidemo.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDto {
    private Long id;
    private Long userId;
    private Long categoryId;
    private double budgetAmount;
    private LocalDate createdAt;
}
