package ru.practicum.events.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.EventUpdateUser;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.service.PrivateEventService;
import ru.practicum.exception.ValidationException;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateEventController {
    private final PrivateEventService privateEventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EventDto> addEvent(@PathVariable Long userId,
                                             @Valid @RequestBody NewEventDto dto) {
        log.info("Добавление нового события {}, пользователем с идентификатором {}", dto, userId);
        if (dto.getEventDate() != null
                && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidationException("Ошибка даты");
        }
        EventDto result = privateEventService.addEvent(userId, dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventDto> update(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @RequestBody @Valid EventUpdateUser eventUpdateUser
    ) {
        log.info("Изменение события {}, пользователем с идентификатором {}", eventId, userId);
        if (eventUpdateUser.getEventDate() != null
                && eventUpdateUser.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidationException("Ошибка даты");
        }
        EventDto result = privateEventService.updateEvent(userId, eventId, eventUpdateUser);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsByUser(
            @PathVariable @Positive Long userId,
            @RequestParam(value = "from", defaultValue = "0", required = false) @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10", required = false) @Positive int size) {
        log.info("Получение событий, добавленных пользователем с идентификатором {}", userId);
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        List<EventShortDto> list = privateEventService.getEventsByUser(userId, pageRequest);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventByUserAndEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение полной информации о событии {}, добавленном пользователем с идентификатором {}", eventId, userId);
        EventDto result = privateEventService.getEventByUserAndEvent(userId, eventId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение информации о запросах на участие в событии {}, пользователя с идентификатором {}", eventId, userId);
        List<ParticipationRequestDto> requests = requestService.getEventRequests(userId, eventId);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PatchMapping({"/{eventId}/requests", "/{eventId}/requests/"})
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequestStatus(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Изменение статуса заявок на участие в событии {}, пользователя с идентификатором {}", eventId,userId);
        EventRequestStatusUpdateResult result = requestService.updateRequestStatus(userId, eventId, eventRequestStatusUpdateRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
