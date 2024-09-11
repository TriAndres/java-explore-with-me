package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public StatsRequestDto addStat(StatsRequestDto statsRequestDto) {
        return StatsMapper.toStatsRequestDto(statsRepository.save(StatsMapper.toStats(statsRequestDto)));
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique != null && unique.equals(true)) {
            if (uris == null) {
                return statsRepository.findAllByTimestampBetweenAndUniqueIp(start, end);
            } else
                return statsRepository.findAllByTimestampBetweenAndUriInAndUniqueIp(start, end, uris);
        } else {
            if (uris == null) {
                return statsRepository.findAllByTimestampBetween(start, end);
            } else
                return statsRepository.findAllByTimestampBetweenAndUriIn(start, end, uris);
        }
    }
}
