package ru.practicum.explorewithme.event.dto;

import ru.practicum.explorewithme.category.dto.CategoryMapper;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.Location;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.user.dto.UserMapper;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn().format(FORMATTER),
                event.getDescription(),
                event.getEventDate().format(FORMATTER),
                UserMapper.toUserShortDto(event.getInitiator()),
                LocationMapper.toLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn().format(FORMATTER),
                event.getRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static Event toEvent(NewEventDto newEventDto, User user, Category category, Location location) {
        Boolean paid;
        Boolean requestModeration;
        Integer participantLimit;
        if (newEventDto.getPaid() == null) {
            paid = false;
        } else {
            paid = newEventDto.getPaid();
        }
        if (newEventDto.getRequestModeration() == null) {
            requestModeration = true;
        } else {
            requestModeration = newEventDto.getRequestModeration();
        }
        if (newEventDto.getParticipantLimit() == null) {
            participantLimit = 0;
        } else {
            participantLimit = newEventDto.getParticipantLimit();
        }
        return new Event(
                null,
                newEventDto.getAnnotation(),
                category,
                0L,
                LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER),
                newEventDto.getDescription(),
                LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER),
                user,
                location,
                paid,
                participantLimit,
                LocalDateTime.now(),
                requestModeration,
                State.PENDING,
                newEventDto.getTitle(),
                0L
        );
    }

    public static List<EventFullDto> listToEventFullDto(List<Event> events) {
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        for (Event event : events) {
            eventFullDtos.add(toEventFullDto(event));
        }
        return eventFullDtos;
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().format(FORMATTER),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static List<EventShortDto> listToEventShortDto(List<Event> events) {
        List<EventShortDto> shortDtos = new ArrayList<>();
        for (Event event : events) {
            shortDtos.add(toEventShortDto(event));
        }
        return shortDtos;
    }

    public static EventCommentDto toEventCommentDto(Event event) {
        return new EventCommentDto(
                event.getId(),
                event.getTitle()
        );
    }
}
