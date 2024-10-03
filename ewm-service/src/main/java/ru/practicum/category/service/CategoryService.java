package ru.practicum.category.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    /**
     * public
     */
    CategoryDto getCategoryById(Long categoryId);

    List<CategoryDto> getAllCategories(PageRequest pageRequest);

    /**
     * admin
     */
    CategoryDto addCategory(NewCategoryDto newCategoryDto);


    void deleteCategory(Long categoryId);

    CategoryDto updateCategory(Long categoryId, NewCategoryDto newCategoryDTO);
}
