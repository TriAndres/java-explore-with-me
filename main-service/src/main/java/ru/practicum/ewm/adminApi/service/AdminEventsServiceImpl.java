package ru.practicum.ewm.adminApi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.adminApi.dto.RequestParamForEvent;
import ru.practicum.ewm.base.dao.EventRepository;
import ru.practicum.ewm.base.dto.event.EventFullDto;
import ru.practicum.ewm.base.dto.event.UpdateEventAdminRequest;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminEventsServiceImpl implements AdminEventsService {

    private final EventRepository eventRepository;

    @Transactional
    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent) {
        return null;
    }

    @Transactional
    @Override
    public List<EventFullDto> getAll(RequestParamForEvent param) {
        return List.of();
    }

    private void checkEventDate(LocalDateTime eventDate) {

    }
}
