package ru.practicum.server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.model.EndpointHitRequestDto;
import ru.practicum.model.StatResponseDto;
import ru.practicum.server.model.*;

@UtilityClass
public class StatsServerMapper {

    public static EndpointHit toEndpointHit(EndpointHitRequestDto requestDto) {
        return EndpointHit.builder()
                .app(requestDto.getApp())
                .uri(requestDto.getUri())
                .ip(requestDto.getIp())
                .timestamp(requestDto.getTimestamp())
                .build();
    }

    public static StatResponseDto toStatResponseDto(StatHits statHits) {
        return new StatResponseDto(statHits.getApp(), statHits.getUri(), statHits.getHits());
    }
}
