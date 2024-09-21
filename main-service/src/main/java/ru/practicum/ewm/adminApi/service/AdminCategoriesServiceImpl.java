package ru.practicum.ewm.adminApi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.dao.CategoriesRepository;
import ru.practicum.ewm.base.dto.category.CategoryDto;
import ru.practicum.ewm.base.dao.EventRepository;
import ru.practicum.ewm.base.dto.category.NewCategoryDto;
import ru.practicum.ewm.base.model.Category;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminCategoriesServiceImpl implements AdminCategoriesService{

    private final CategoriesRepository categoriesRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto dto) {
        return null;
    }

    @Transactional
    @Override
    public void delete(Long catId) {

    }

    @Transactional
    @Override
    public CategoryDto update(NewCategoryDto dto, Long catId) {
        return null;
    }

    private Category get(Long id){
        return null;
    }
}
