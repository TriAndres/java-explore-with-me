package ru.practicum.statsserver.stats.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.statsserver.stats.model.EndpointHit;

import java.time.format.DateTimeFormatter;

@Component
public class EndpointHitMapper {
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public EndpointHit toEndpointHit(EndpointHitDto dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        return EndpointHit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }
}
