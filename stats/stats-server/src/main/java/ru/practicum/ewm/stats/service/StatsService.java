package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.stats.StatsDto;
import ru.practicum.ewm.stats.StatsRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    StatsRequestDto addStats(StatsRequestDto statsRequestDto);

    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}