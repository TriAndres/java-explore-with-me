package ru.practicum.ewm.adminApi.service;

import ru.practicum.ewm.base.dto.compilation.CompilationDto;
import ru.practicum.ewm.base.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.base.dto.compilation.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);
}