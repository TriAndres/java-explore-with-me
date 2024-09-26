package ru.practicum.ewm.publicApi.service;

import ru.practicum.ewm.base.dto.category.CategoryDto;

import java.util.List;

public interface PublicCategoriesService {
    List<CategoryDto> getAll(int from, int size);

    CategoryDto get(Long catId);
}