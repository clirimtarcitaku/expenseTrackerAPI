package org.example.projektidemo.service.impl;

import lombok.AllArgsConstructor;
import org.example.projektidemo.dto.CategoryDto;
import org.example.projektidemo.entity.Category;
import org.example.projektidemo.exception.ResourceNotFoundException;
import org.example.projektidemo.mapper.CategoryMapper;
import org.example.projektidemo.repository.CategoryRepository;
import org.example.projektidemo.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.mapToCategory(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.mapToCategoryDto(savedCategory);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + id)
        );
        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMapper::mapToCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + id)
        );
        category.setCategoryName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.mapToCategoryDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + id)
        );
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto getMostFrequentCategory() {
        Object mostFrequentCategory = categoryRepository.findMostFrequentCategory();
        Long categoryId = (Long) ((Object []) mostFrequentCategory)[0];
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + categoryId)
        );
        return CategoryMapper.mapToCategoryDto(category);
    }


}
