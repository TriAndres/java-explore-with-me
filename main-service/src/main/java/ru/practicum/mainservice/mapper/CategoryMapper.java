package ru.practicum.mainservice.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.category.*;
import ru.practicum.mainservice.model.Category;

@UtilityClass
public class CategoryMapper {

    public static Category toCategory(CategoryCreationDto categoryCreationDto) {
        return Category.builder()
                .name(categoryCreationDto.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
