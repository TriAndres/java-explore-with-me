package ru.practicum.ewm.adminApi.service;

import ru.practicum.ewm.base.dto.category.CategoryDto;
import ru.practicum.ewm.base.dto.category.NewCategoryDto;

public interface AdminCategoriesService {
    CategoryDto create(NewCategoryDto dto);

    void delete(Long catId);

    CategoryDto update(NewCategoryDto dto, Long catId);
}
