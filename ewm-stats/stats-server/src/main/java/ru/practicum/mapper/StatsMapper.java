package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.EndpointHitDto;


@Mapper(componentModel = "spring")
public interface StatsMapper {
    EndpointHitDto toEndpointHitDto(EndpointHit endpointHit);

    EndpointHit toEndpointHit(EndpointHitDto endpointHitDto);
}
