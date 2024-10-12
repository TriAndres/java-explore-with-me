package ru.practicum.explorewithme.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.request.service.ParticipationRequestService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final ParticipationRequestService participationRequestService;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable(name = "userId") Long userId,
                                 @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Получен запрос POST /users/" + userId + "/events");
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByCurrentUser(@PathVariable Long userId,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /users/" + userId + "/events");
        return eventService.findEventsByCurrentUser(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByCurrentUser(@PathVariable Long userId,
                                              @PathVariable Long eventId) {
        log.info("Получен запрос GET /users/" + userId + "/events/" + eventId);
        return eventService.findEventByCurrentUser(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto patchEvent(@PathVariable Long userId,
                                   @PathVariable Long eventId,
                                   @Valid @RequestBody UpdateEventUserRequest request) {
        log.info("Получен запрос PATCH /users/" + userId + "/events/" + eventId);
        return eventService.updateEvent(userId, eventId, request);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable Long userId,
                                                                  @PathVariable Long eventId) {
        log.info("Получен запрос GET /users/" + userId + "/events/" + eventId + "/requests");
        return participationRequestService.getParticipationRequests(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateParticipationRequest(@PathVariable Long userId,
                                                                     @PathVariable Long eventId,
                                                                     @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Получен запрос PATCH /users/" + userId + "/events/" + eventId + "/requests");
        return participationRequestService.updateParticipationRequest(userId, eventId, request);
    }


    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsPublic(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(defaultValue = "true") Boolean onlyAvailable,
                                               @RequestParam(required = false) String sort,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest request) {
        log.info("Получен запрос GET /events");
        return eventService.findEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventPublic(@PathVariable Long id,
                                       HttpServletRequest request) {
        log.info("Получен запрос GET /events/" + id);
        return eventService.findEventPublic(id, request);
    }

    @GetMapping("/admin/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsAdmin(@RequestParam(required = false) List<Long> users,
                                             @RequestParam(required = false) List<String> states,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) String rangeStart,
                                             @RequestParam(required = false) String rangeEnd,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /admin/events");
        return eventService.findEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto patchEventAdmin(@PathVariable Long eventId,
                                        @Valid @RequestBody UpdateEventAdminRequest request) {
        log.info("Получен запрос PATCH /admin/events/{eventId}");
        return eventService.patchEventAdmin(eventId, request);
    }

    @GetMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUsersRequests(@PathVariable Long userId) {
        log.info("Получен запрос GET /users/" + userId + "/events");
        return participationRequestService.getUsersRequests(userId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        log.info("Получен запрос POST /users/" + userId + "/events, eventId = " + eventId);
        return participationRequestService.createRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Получен запрос PATCH /users/" + userId + "/requests/" + requestId + "cancel");
        return participationRequestService.cancelRequest(userId, requestId);
    }
}