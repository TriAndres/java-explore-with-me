package ru.practicum.explorewithme.category.service;

import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto patchCategory(Long id, CategoryDto categoryDto);

    void deleteCategory(Long id);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(Long id);
}
