package ru.practicum.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.EndpointHitRequestDto;
import ru.practicum.model.StatResponseDto;
import ru.practicum.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void saveHit(@RequestBody @Valid EndpointHitRequestDto requestDto) {
        statsService.addHit(requestDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatResponseDto>> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                          @RequestParam(defaultValue = "") List<String> uris,
                                                          @RequestParam(defaultValue = "false") boolean unique) {
        if (end.isBefore(start)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(statsService.getStats(start, end, uris, unique));
    }
}
