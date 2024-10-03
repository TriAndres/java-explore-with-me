package ru.practicum.events.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.EventUpdateUser;
import ru.practicum.events.dto.NewEventDto;

import java.util.List;

public interface PrivateEventService {
    EventDto addEvent(Long userId, NewEventDto eventDto);

    EventDto updateEvent(Long userId, Long eventId, EventUpdateUser eventUpdateUser);

    List<EventShortDto> getEventsByUser(Long userId, PageRequest pageRequest);

    EventDto getEventByUserAndEvent(Long userId, Long eventId);
}
