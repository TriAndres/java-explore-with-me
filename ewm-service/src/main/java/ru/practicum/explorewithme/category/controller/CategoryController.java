package ru.practicum.explorewithme.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.service.CategoryService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Получен запрос POST /admin/categories");
        return categoryService.addCategory(newCategoryDto);
    }

    @PatchMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto patchCategory(@PathVariable(name = "catId") Long id,
                                     @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Получен запрос PATCH /admin/categories/" + id);
        return categoryService.patchCategory(id, categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(name = "catId") Long id) {
        log.info("Получен запрос DELETE /admin/categories/" + id);
        categoryService.deleteCategory(id);
    }

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /categories");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable(name = "catId") Long id) {
        log.info("Получен запрос GET /categories/{catId}");
        return categoryService.getCategoryById(id);
    }
}