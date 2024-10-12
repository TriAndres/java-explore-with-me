package ru.practicum.explorewithme.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.CategoryMapper;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.common.util.PageCreatorUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(CategoryMapper.toCategory(newCategoryDto));
        log.info("Добавлена категория: " + category);
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto patchCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Категории с id = " + id + " не найдено"));
        log.info("Обновляем категорию с id = " + id);
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Категории с id = " + id + " не найдено"));
        log.info("Удаляем категорию с id = " + id);
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        PageRequest page = PageCreatorUtil.createPage(from, size);
        List<Category> categories = categoryRepository.findAllBy(page);
        log.info("Возвращаю информацию по категориям");
        return CategoryMapper.listToCategoryDto(categories);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Категории с id = " + id + " не найдено"));
        log.info("Возвращаю информацию по категории");
        return CategoryMapper.toCategoryDto(category);
    }
}
