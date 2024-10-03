package ru.practicum.compilations.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationUpdateDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.service.AdminCompilationServiceImpl;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminCompilationController {
    private final AdminCompilationServiceImpl adminCompilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> addCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Добавление новой подборки: {}", newCompilationDto);
        CompilationDto result = adminCompilationService.addCompilation(newCompilationDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{compilationId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable Long compilationId) {
        log.info("Удаление подборки с идентификатором: {}", compilationId);
        adminCompilationService.deleteCompilation(compilationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compilationId}")
    public ResponseEntity<CompilationDto> updateCompilation(@PathVariable Long compilationId,
                                                            @RequestBody @Valid CompilationUpdateDto compilationUpdateDto) {
        log.info("Обновление информации о подборке с идентификатором: {}", compilationId);
        CompilationDto result = adminCompilationService.updateCompilation(compilationId, compilationUpdateDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
