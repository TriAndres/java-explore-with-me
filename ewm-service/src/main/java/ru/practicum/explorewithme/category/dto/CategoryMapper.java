package ru.practicum.explorewithme.category.dto;

import ru.practicum.explorewithme.category.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getId(),
                categoryDto.getName()
        );
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(
                null,
                newCategoryDto.getName()
        );
    }

    public static List<Category> listToCategroy(List<CategoryDto> categoryDtos) {
        List<Category> categories = new ArrayList<>();
        for (CategoryDto categoryDto : categoryDtos) {
            categories.add(toCategory(categoryDto));
        }
        return categories;
    }

    public static List<CategoryDto> listToCategoryDto(List<Category> categories) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            categoryDtos.add(toCategoryDto(category));
        }
        return categoryDtos;
    }
}