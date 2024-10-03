package ru.practicum.events.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventUpdateAdmin;
import ru.practicum.events.model.AdminEventParams;

import java.util.List;

public interface AdminEventService {
    List<EventDto> getAllAdminEvents(AdminEventParams adminEventParams, PageRequest pageRequest);

    EventDto updateEvent(Long eventId, EventUpdateAdmin eventUpdateAdmin);
}
