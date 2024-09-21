package ru.practicum.ewm.adminApi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.dao.CompilationRepository;
import ru.practicum.ewm.base.dao.EventRepository;
import ru.practicum.ewm.base.dto.compilation.CompilationDto;
import ru.practicum.ewm.base.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.base.dto.compilation.UpdateCompilationRequest;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminCompilationServiceImpl implements AdminCompilationService{

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        return null;
    }

    @Override
    public void delete(Long compId) {

    }

    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        return null;
    }
}