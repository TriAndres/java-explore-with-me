package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ViewStatsDto;
import ru.practicum.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final HitRepository hitRepository;

    @Override
    public EndpointHitDto saveHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.mapToEndpointHit(endpointHitDto);
        hitRepository.save(endpointHit);
        return EndpointHitMapper.mapToEndpointHitDto(endpointHit);

    }

    @Override
    public List<ViewStatsDto> findHitsByParams(LocalDateTime start, LocalDateTime end, List<String> uris,
                                               Boolean unique
    ) {
        if (Boolean.TRUE.equals(unique)) {
            List<ViewStatsDto> test = hitRepository.findAllUnique(start, end, uris);
            return test;
        } else {
            List<ViewStatsDto> test2 = hitRepository.findAll(start, end, uris);
            return test2;
        }
    }
}
