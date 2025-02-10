package org.example.projektidemo.mapper;

import org.example.projektidemo.dto.CategoryDto;
import org.example.projektidemo.entity.Category;

public class CategoryMapper {
    public static CategoryDto mapToCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getCategoryName());
    }

    public static Category mapToCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }
}
