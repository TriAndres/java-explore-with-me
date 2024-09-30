package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.InvalidDateRangeException;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ViewStatsDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final StatsService statsService;


    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> saveHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("StatisticController, saveHit, Request body app: {}, uri: {}, ip: {}, timestamp: {}",
                endpointHitDto.getApp(), endpointHitDto.getUri(), endpointHitDto.getIp(), endpointHitDto.getTimestamp());
        statsService.saveHit(endpointHitDto);
        return new ResponseEntity<>(endpointHitDto, HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(
            @RequestParam("start") @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
            @RequestParam(required = false, value = "uris") List<String> uris,
            @RequestParam(required = false, value = "unique") boolean unique
    ) {
        log.info("Statistic Controller, getStats, parameters: start {}, end {}, uris {}, unique {}",
                start, end, uris, unique);
        if (start.isAfter(end)) {
            throw new InvalidDateRangeException("Ошибка даты.");
        }
        List<ViewStatsDto> statsList = statsService.findHitsByParams(start, end, uris, unique);
        return new ResponseEntity<>(statsList, HttpStatus.OK);
    }
}
