package ru.practicum.server.service;

import ru.practicum.model.EndpointHitRequestDto;
import ru.practicum.model.StatResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void addHit(EndpointHitRequestDto requestDto);

    List<StatResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
