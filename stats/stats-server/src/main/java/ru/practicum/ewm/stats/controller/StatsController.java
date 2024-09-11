package ru.practicum.ewm.stats.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.StatsConstants;
import ru.practicum.ewm.exception.error.ValidationException;
import ru.practicum.ewm.stats.StatsDto;
import ru.practicum.ewm.stats.StatsRequestDto;
import ru.practicum.ewm.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.StatsConstants.API_HIT_PREFIX;
import static ru.practicum.ewm.StatsConstants.API_STATS_PREFIX;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping(API_HIT_PREFIX)
    @ResponseStatus(HttpStatus.CREATED)
    public StatsRequestDto addStats(@Valid @RequestBody StatsRequestDto statsRequestDto) {
        return statsService.addStats(statsRequestDto);
    }

    @GetMapping(API_STATS_PREFIX)
    public List<StatsDto> getStats(@RequestParam("start") @DateTimeFormat(pattern = StatsConstants.DATA_PATTERN) LocalDateTime start,
                                   @RequestParam("end") @DateTimeFormat(pattern = StatsConstants.DATA_PATTERN) LocalDateTime end,
                                   @RequestParam(required = false, value = "uris") List<String> uris,
                                   @RequestParam(required = false, value = "unique") Boolean unique) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new ValidationException();
        }
        return statsService.getStats(start, end, uris, unique);
    }
}