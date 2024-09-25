package ru.practicum.ewm.adminApi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminApi.service.AdminCategoriesService;
import ru.practicum.ewm.base.dto.category.CategoryDto;
import ru.practicum.ewm.base.dto.category.NewCategoryDto;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    public final AdminCategoriesService service;

    @PostMapping
    public ResponseEntity<CategoryDto> crete(@RequestBody @Valid NewCategoryDto dto) {
        log.info("Получение запроса post /admin/categories с новой катигорией: {}", dto.getName());
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> delete(@PathVariable Long catId) {
        log.info("Получение запроса delete /admin/categories/{}", catId);
        service.delete(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> update(@RequestBody @Valid NewCategoryDto dto,
                                                @PathVariable Long catId) {
        log.info("Полученый запрос patch /admin/categories/{} на изменение категории: {}",catId, dto.getName());
        return new ResponseEntity<>(service.update(dto, catId), HttpStatus.OK);
    }
}
