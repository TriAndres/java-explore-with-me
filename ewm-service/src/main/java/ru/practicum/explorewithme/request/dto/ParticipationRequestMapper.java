package ru.practicum.explorewithme.request.dto;

import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.request.model.ParticipationRequest;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ParticipationRequestMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequest toParticipationRequest(ParticipationRequestDto participationRequestDto, User user, Event event) {
        return new ParticipationRequest(
                participationRequestDto.getId(),
                LocalDateTime.parse(participationRequestDto.getCreated(), FORMATTER),
                event,
                user,
                State.valueOf(participationRequestDto.getStatus()));
    }

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated().format(FORMATTER),
                request.getEvent().getId(),
                request.getRequester().getId(),
                String.valueOf(request.getStatus())
        );
    }

    public static List<ParticipationRequestDto> listToParticipationRequestDto(List<ParticipationRequest> requests) {
        List<ParticipationRequestDto> dtos = new ArrayList<>();
        for (ParticipationRequest participationRequest : requests) {
            dtos.add(toParticipationRequestDto(participationRequest));
        }
        return dtos;
    }
}
