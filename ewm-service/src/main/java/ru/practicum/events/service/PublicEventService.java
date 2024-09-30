package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.UserEventParams;

import java.util.List;

public interface PublicEventService {
    EventDto getEventById(Long eventId, HttpServletRequest request);

    List<EventShortDto> getAllPublicEvents(UserEventParams userEventParams, PageRequest pageRequest);
}