package ru.practicum.requests.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.Request;

import java.time.format.DateTimeFormatter;

@Component
public class RequestMapper {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ParticipationRequestDto requestToParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated().format(DATE_TIME_FORMATTER),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus().toString());
    }
}