package ru.practicum.explorewithme.model;

import ru.practicum.explorewithme.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EndpointHitMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndpointHit toEndpointHit(EndpointHitDto hitDto) {
        return new EndpointHit(
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                LocalDateTime.parse(hitDto.getTimestamp(), FORMATTER)
        );
    }

    public static EndpointHitDto toEndpointHitDto(EndpointHit hit) {
        return new EndpointHitDto(
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp().format(FORMATTER)
        );
    }

    public static List<EndpointHitDto> listToEndpointHitDto(Iterable<EndpointHit> hits) {
        List<EndpointHitDto> dtos = new ArrayList<>();
        for (EndpointHit hit : hits) {
            dtos.add(toEndpointHitDto(hit));
        }
        return dtos;
    }
}