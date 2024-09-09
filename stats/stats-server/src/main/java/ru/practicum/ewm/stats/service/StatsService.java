package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.stats.StatsDTO;
import ru.practicum.ewm.stats.StatsRequestDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatsService {
    StatsRequestDto addStats(StatsRequestDto statsRequestDto);

    Collection<StatsDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
