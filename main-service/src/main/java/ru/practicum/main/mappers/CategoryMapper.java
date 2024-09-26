package ru.practicum.main.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.dto.category.NewCategoryDto;
import ru.practicum.main.models.Category;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(NewCategoryDto newCategoryDto);

    List<CategoryDto> toCategoryDtoList(List<Category> categoryList);

    CategoryDto toCategoryDto(Category save);
}