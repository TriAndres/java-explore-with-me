package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.StatsDTO;
import ru.practicum.ewm.stats.StatsRequestDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    @Override
    public StatsRequestDto addStats(StatsRequestDto statsRequestDto) {
        return null;
    }

    @Override
    public Collection<StatsDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return List.of();
    }
}
