package ru.practicum.statsserver.stats.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

public interface StatsService {
    void create(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique);
}
