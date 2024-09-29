package ru.practicum.service;

import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    EndpointHitDto saveHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> findHitsByParams(LocalDateTime start, LocalDateTime end, List<String> uris,
                                        Boolean unique);
}
