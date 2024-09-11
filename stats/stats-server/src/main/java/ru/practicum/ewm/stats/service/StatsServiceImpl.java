package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.StatsDto;
import ru.practicum.ewm.stats.StatsRequestDto;
import ru.practicum.ewm.stats.mapper.StatsMapper;
import ru.practicum.ewm.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Transactional
    @Override
    public StatsRequestDto addStats(StatsRequestDto statsRequestDto) {
        return StatsMapper.toStatsRequestDto(statsRepository.save(StatsMapper.toStats(statsRequestDto)));
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique != null && unique.equals(true)) {
            if (uris == null) {
                return statsRepository.findAllByTimestampBetweenAndUniqueIp(start, end);
            } else {
                return statsRepository.findAllByTimestampBetweenAndUriInAndUniqueIp(start,end, uris);
            }
        } else {
            if (uris == null) {
                return statsRepository.findAllByTimestampBetween(start, end);
            } else {
                return statsRepository.findAllByTimestampBetweenAndUriIn(start, end, uris);
            }
        }
    }
}