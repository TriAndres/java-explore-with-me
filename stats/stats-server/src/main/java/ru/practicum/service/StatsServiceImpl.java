package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.App;
import ru.practicum.model.Stats;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.AppRepository;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final AppRepository appRepository;

    @Override
    @Transactional
    public EndpointHitDto createHit(EndpointHitDto endpointHitDto) {
        String nameApp = endpointHitDto.getApp();
        Optional<App> appName = appRepository.findByName(nameApp);
        App app;
        if (appName.isEmpty()) {
            app = appRepository.save(new App(nameApp));
        } else {
            app = appName.orElseThrow(() -> new NotFoundException("App not found: " + nameApp));
        }
        Stats saved = statsRepository.save(StatsMapper.toStats(endpointHitDto, app));
        return StatsMapper.toEndpointHitDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStatsDto> getStats(StatsDto statsDto) {
        List<ViewStats> stats;
        Boolean unique = statsDto.getUnique();
        List<String> uris = statsDto.getUris();
        LocalDateTime start = statsDto.getStart();
        LocalDateTime end = statsDto.getEnd();
        if (start.isAfter(end)) {
            throw new ValidationException("Начало диапазона не может быть позже конца диапазона.");
        }
        if (unique) {
            if (uris == null) {
                stats = statsRepository.findStatsUniqueWithOutUris(start, end);
            } else {
                stats = statsRepository.findStatsUnique(start, end, uris);
            }
        } else {
            if (uris == null) {
                stats = statsRepository.findStatsWithOutUris(start, end);
            } else {
                stats = statsRepository.findStats(start, end, uris);
            }
        }
        return StatsMapper.toViewStatsDtos(stats);
    }

}
