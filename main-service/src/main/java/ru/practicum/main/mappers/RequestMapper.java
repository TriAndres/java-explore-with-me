package ru.practicum.main.mappers;

import org.mapstruct.Mapper;
import ru.practicum.main.dto.request.RequestDto;
import ru.practicum.main.models.Request;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestDto toRequestDto(Request request);

    List<RequestDto> toRequestDtoList(List<Request> requests);
}
