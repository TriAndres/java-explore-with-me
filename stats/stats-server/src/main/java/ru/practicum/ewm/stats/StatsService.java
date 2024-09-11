package ru.practicum.ewm.stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    StatsRequestDto addStat(StatsRequestDto statsRequestDto);

    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
