package ru.practicum.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.model.EndpointHitRequestDto;
import ru.practicum.model.StatResponseDto;
import ru.practicum.server.mapper.StatsServerMapper;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.model.StatHits;
import ru.practicum.server.repository.StatsServerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements StatsService {
    @Autowired
    private StatsServerRepository statsServerRepository;

    @Override
    public void addHit(EndpointHitRequestDto requestDto) {
        EndpointHit endpointHit = StatsServerMapper.toEndpointHit(requestDto);
        statsServerRepository.save(endpointHit);
    }

    @Override
    public List<StatResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<StatHits> stats;

        if (unique) {
            stats = statsServerRepository.findUniqueStatsWithUris(start, end, uris);
        } else {
            stats = statsServerRepository.findAllStatsWithUris(start, end, uris);
        }

        return stats.stream().map(StatsServerMapper::toStatResponseDto).collect(Collectors.toList());
    }
}
