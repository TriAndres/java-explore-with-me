package ru.practicum.mapper;

import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.App;
import ru.practicum.model.Stats;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class StatsMapper {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private StatsMapper() {
    }

    public static Stats toStats(EndpointHitDto endpointHitDto, App app) {
        return Stats.builder()
                .app(app)
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), formatter))
                .build();
    }

    public static EndpointHitDto toEndpointHitDto(Stats stats) {
        return EndpointHitDto.builder()
                .app(stats.getApp().getName())
                .uri(stats.getUri())
                .ip(stats.getIp())
                .timestamp(stats.getTimestamp().toString())
                .build();
    }

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }

    public static List<ViewStatsDto> toViewStatsDtos(List<ViewStats> viewStats) {
        return viewStats.stream().map(StatsMapper::toViewStatsDto).collect(Collectors.toList());
    }

}
