package ru.practicum.ewm.adminApi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminApi.service.AdminCompilationService;
import ru.practicum.ewm.base.dto.compilation.CompilationDto;
import ru.practicum.ewm.base.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.base.dto.compilation.UpdateCompilationRequest;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {

    private final AdminCompilationService service;

    @PostMapping
    public ResponseEntity<CompilationDto> save(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Получен запрос post /admin/compilations с новой подборкой {}", newCompilationDto.getTitle());
        return new ResponseEntity<>(service.save(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> delete(@PathVariable Long compId) {
        log.info("Получен запрос deleter /admin/compilations/{}", compId);
        service.delete(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> update(@PathVariable Long compId,
                                                 @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("Получен запрос patch /admin/compilations/{} на изменение подборки", compId);
        return new ResponseEntity<>(service.update(compId, updateCompilationRequest), HttpStatus.OK);
    }
}