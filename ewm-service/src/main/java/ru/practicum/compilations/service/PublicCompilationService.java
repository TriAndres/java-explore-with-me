package ru.practicum.compilations.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.compilations.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {

    List<CompilationDto> getAllCompilations(Boolean pined, PageRequest pageRequest);

    CompilationDto getCompilationById(Long compilationId);
}
