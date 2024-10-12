package ru.practicum.explorewithme.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.explorewithme.event.dto.*;

import java.util.List;

public interface EventService {
    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> findEventsByCurrentUser(Long userId, int from, int size);

    EventFullDto findEventByCurrentUser(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest request);

    List<EventShortDto> findEventsPublic(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, int from, int size, HttpServletRequest request);

    EventFullDto findEventPublic(Long id, HttpServletRequest request);

    List<EventFullDto> findEventsAdmin(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, int from, int size);

    EventFullDto patchEventAdmin(Long eventId, UpdateEventAdminRequest request);
}
