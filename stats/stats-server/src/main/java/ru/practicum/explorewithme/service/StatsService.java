package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.ViewStatsDto;
import ru.practicum.explorewithme.model.HitResponse;

import java.util.List;

public interface StatsService {

    HitResponse addEndpointHit(EndpointHitDto hitDto);

    List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique);

    Boolean checkUnique(String uri, String ip);
}